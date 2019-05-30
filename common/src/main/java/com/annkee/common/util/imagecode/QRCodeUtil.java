package com.annkee.common.util.imagecode;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.ServiceException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSErrorCode;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.annkee.common.constant.ConfigFileProperty;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具
 *
 * @author wangan
 * @date 2018/7/11
 */
@Slf4j
@Component
public class QRCodeUtil {
    
    /**
     * 二维码写码器
     */
    private static MultiFormatWriter multiWriter = new MultiFormatWriter();
    
    private static final String CHARSET = "utf-8";
    
    private static final String FORMAT_NAME = ".jpg";
    
    @Resource
    private ConfigFileProperty configFileProperty;
    
    /**
     * 阿里云OSS_ENDPOINT  青岛Url
     */
    private static String OSS_ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    private static String ossPath = "http://annkee.oss-cn-beijing.aliyuncs.com/";
    
    /**
     * 阿里云BUCKET_NAME  OSS
     */
    private static String BUCKET_NAME = "annkee";
    
    /**
     * 二维码尺寸
     */
    private final int QRCODE_SIZE = 175;
    
    public byte[] readInputStream(InputStream inStream) throws Exception {
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
     * @param backgroundImage 最底下的图
     * @param bufferedImage   上面的图
     * @throws Exception
     */
    private String insertImage(String backgroundImage, BufferedImage bufferedImage) {
        try {
            //new一个URL对象
            URL url = new URL(backgroundImage);
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
            
            // 参数为空
            File directory = new File("");
            String courseFile = null;
            try {
                courseFile = directory.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.warn("dirPath is =================={}", courseFile);
            // 文件上传后的路径
            String filePath = "";
            String fileName = new StringBuffer(System.nanoTime() + ".jpg").toString();
            if (courseFile.startsWith("/")) {
                filePath = "/opt/ow/webapps/images/fosun-pdf-files/" + fileName;
            } else {
                filePath = "D:/downloads/" + fileName;
            }
            
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            File file = new File(filePath);
            
            //创建输出流
            FileOutputStream outStream = new FileOutputStream(file);
            //写入数据
            outStream.write(data);
            //关闭输出流
            outStream.close();
            
            BufferedImage background = null;
            try {
                
                Image src = Toolkit.getDefaultToolkit().getImage(filePath);
                BufferedImage image = toBufferedImage(src);
                background = image;
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            int width = bufferedImage.getWidth(null);
            int height = bufferedImage.getHeight(null);
            int backgroundWidth = background.getWidth(null);
            int backgroundHeight = background.getHeight(null);
            
            // 插入二维码
            Graphics2D graph = background.createGraphics();
            int x = 0;
            int y = 0;
            if (backgroundImage.contains("img_bg_fenxiang")) {
                //二维码放中间
                x = (backgroundWidth - width) / 2;
                y = backgroundHeight - height - 164;
            } else {
                //二维码位置调整
                x = 45;
                y = backgroundHeight - height - 31;
            }
            graph.drawImage(bufferedImage, x, y, width, height, null);
            Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
            graph.setStroke(new BasicStroke(3f));
            graph.draw(shape);
            graph.dispose();
            ImageIO.write(background, "png", new File(filePath));
            String urlPath = getUrlPath(filePath);
            //关闭输出流
            File fileToDelte = new File(filePath);
            if (fileToDelte.exists()) {
                fileToDelte.delete();
            }
            return urlPath;
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        return null;
    }
    
    public BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        
        // Create a buffered image using the default color model
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }
    
    /**
     * 生成带logo二维码，并保存到磁盘
     *
     * @param content 二维码内容
     * @param backImg 上面的图片
     * @throws Exception
     */
    public String encode(String content, String backImg) throws Exception {
        String imagePath = genBarcode(content, backImg);
        return imagePath;
    }
    
    public String genBarcode(String content, String backImg) throws WriterException, IOException {
        Map<EncodeHintType, Object> hint = new HashMap<>(5);
        hint.put(EncodeHintType.CHARACTER_SET, CHARSET);
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
                    Color color = new Color(0, 0, 0);
                    int colorInt = color.getRGB();
                    data[y * QRCODE_SIZE + x] = colorInt;
                    
                } else {
                    //白色
                    data[y * w + x] = -1;
                }
            }
        }
        BufferedImage image = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE,
                BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, QRCODE_SIZE, QRCODE_SIZE, data);
        //背景图上插入二维码
        String path = insertImage(backImg, image);
        return path;
    }
    
    
    public String getUrlPath(String uploadFilePath) {
        
        String Objectkey = new StringBuffer("mydoc/").append(System.nanoTime()).append(FORMAT_NAME).toString();
        String path = new StringBuffer(ossPath).append(Objectkey).toString();
        
        // 使用默认的OSS服务器地址创建OSSClient对象,OSS_ENDPOINT代表使用北京节点，北京节点要加上不然包异常
        OSSClient client = new OSSClient(OSS_ENDPOINT, configFileProperty.getAccessKeyId(), configFileProperty.getAccessKeySecret());
        
        try {
            log.warn("正在上传阿里云OSS...");
            uploadFile(client, BUCKET_NAME, Objectkey, uploadFilePath);
            log.warn("上传结束...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    
    /**
     * 创建Bucket
     *
     * @param client     OSSClient对象
     * @param bucketName BUCKET名
     * @throws OSSException
     * @throws ClientException
     */
    public void ensureBucket(OSSClient client, String bucketName) throws OSSException, ClientException {
        try {
            client.createBucket(bucketName);
        } catch (ServiceException e) {
            if (!OSSErrorCode.BUCKES_ALREADY_EXISTS.equals(e.getErrorCode())) {
                throw e;
            }
        }
    }
    
    /**
     * 把Bucket设置成所有人可读
     *
     * @param client     OSSClient对象
     * @param bucketName Bucket名
     * @throws OSSException
     * @throws ClientException
     */
    private void setBucketPublicReadable(OSSClient client, String bucketName) throws
            OSSException, ClientException {
        //创建bucket
        client.createBucket(bucketName);
        
        //设置bucket的访问权限， public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }
    
    /**
     * 上传文件
     *
     * @param client     OSSClient对象
     * @param bucketName Bucket名
     * @param Objectkey  上传到OSS起的名
     * @param filename   本地文件名
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    private void uploadFile(OSSClient client, String bucketName, String Objectkey, String filename)
            throws OSSException, ClientException, FileNotFoundException {
        File file = new File(filename);
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        //判断上传类型，多的可根据自己需求来判定
        if (filename.endsWith("xml")) {
            objectMeta.setContentType("text/xml");
        } else if (filename.endsWith("jpg")) {
            objectMeta.setContentType("image/jpeg");
        } else if (filename.endsWith("png")) {
            objectMeta.setContentType("image/png");
        }
        
        InputStream input = new FileInputStream(file);
        client.putObject(bucketName, Objectkey, input, objectMeta);
    }
    
    
}