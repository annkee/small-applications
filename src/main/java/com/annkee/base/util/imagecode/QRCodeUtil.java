package com.annkee.base.util.imagecode;

import com.alibaba.fastjson.JSONObject;
import com.annkee.base.constant.ProjectConstant;
import com.annkee.base.util.HttpClientUtil;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码工具
 *
 * @author wangan
 * @date 2018/7/11
 */
@Slf4j
public class QRCodeUtil {

    /**
     * 二维码写码器
     */
    private static MultiFormatWriter multiWriter = new MultiFormatWriter();


    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";

    /**
     * 二维码尺寸
     */
    private static final int QRCODE_SIZE = 300;

    /**
     * LOGO宽度
     */
    private static final int WIDTH = 50;

    /**
     * LOGO高度
     */
    private static final int HEIGHT = 50;

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * 在生成的二维码中插入图片
     *
     * @param source
     * @param imgPath
     * @param needCompress
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) {
        try {

            //new一个URL对象
            URL url = new URL(imgPath);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);

            //new一个文件对象用来保存图片，默认保存当前工程根目录
            File file = new File("imageLogo.jpg");
            //创建输出流
            FileOutputStream outStream = new FileOutputStream(file);
            //写入数据
            outStream.write(data);
            //关闭输出流
            outStream.close();

            if (!file.exists()) {
                log.error("" + imgPath + " 该文件不存在！");
                return;
            }

            Image src = null;
            try {
                src = ImageIO.read(new File("imageLogo.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            if (needCompress) {
                // 压缩LOGO
                if (width > WIDTH) {
                    width = WIDTH;
                }
                if (height > HEIGHT) {
                    height = HEIGHT;
                }

                Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                // 绘制缩小后的图
                g.drawImage(image, 0, 0, null);
                g.dispose();
                src = image;
            }

            // 插入LOGO
            Graphics2D graph = source.createGraphics();
            int x = (QRCODE_SIZE - width) / 2;
            int y = (QRCODE_SIZE - height) / 2;
            graph.drawImage(src, x, y, width, height, null);
            Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
            graph.setStroke(new BasicStroke(3f));
            graph.draw(shape);
            graph.dispose();

            file.delete();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /**
     * 生成带logo二维码，并保存到磁盘
     *
     * @param content
     * @param imgPath      logo图片
     * @param destPath
     * @param needCompress
     * @throws Exception
     */
    public static String encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = genBarcode(content, imgPath, needCompress);

        mkdirs(destPath);
        //生成文件名
        String fileName = "qrcode.jpg";
        File file = new File(destPath + "/" + fileName);
        ImageIO.write(image, FORMAT_NAME, file);

        HashMap<String, Object> headers = new HashMap<>(3);
        headers.put("secret-key", ProjectConstant.SECRET_KEY);
        headers.put("identity-id", ProjectConstant.IDENTITY_ID);
        String uploadFile = HttpClientUtil.uploadFile(ProjectConstant.FINAL_URL, headers, file);

        JSONObject jsonObject = JSONObject.parseObject(uploadFile);
        JSONObject data = jsonObject.getJSONObject("data");
        String path = data.getString("path");
        return path;
    }

    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir。(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }


    /**
     * 从二维码中，解析数据
     *
     * @param file 二维码图片文件
     * @return 返回从二维码中解析到的数据值
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }

    public static BufferedImage genBarcode(String content, String imgPath, boolean needCompress) throws WriterException, IOException {
        Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>(5);
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 生成二维码
        BitMatrix matrix = multiWriter.encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hint);
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] data = new int[w * h];
        boolean flag1 = true;
        int stopx = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (matrix.get(x, y)) {
                    if (flag1) {
                        flag1 = false;
                    }
                } else {
                    if (flag1 == false) {
                        stopx = x;
                        break;
                    }
                }
            }
            if (flag1 == false) {
                break;
            }
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (matrix.get(x, y)) {
                    if ((x < stopx) && (y < stopx)) {
                        Color color = new Color(231, 144, 56);
                        int colorInt = color.getRGB();
                        data[y * QRCODE_SIZE + x] = colorInt;
                    } else {
                        int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight() * (y + 1));
                        int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight() * (y + 1));
                        int num3 = (int) (162 - (162.0 - 107.0) / matrix.getHeight() * (y + 1));
                        Color color = new Color(num1, num2, num3);
                        int colorInt = color.getRGB();
                        data[y * w + x] = colorInt;
                    }
                } else {
                    //白色
                    data[y * w + x] = -1;
                }
            }
        }
        BufferedImage image = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE,
                BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, QRCODE_SIZE, QRCODE_SIZE, data);
        insertImage(image, imgPath, needCompress);
        return image;
    }

}