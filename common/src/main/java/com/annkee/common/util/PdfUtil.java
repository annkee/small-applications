package com.annkee.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * pdf转换工具
 *
 * @author wangan
 * @date 2018/12/12
 */
public class PdfUtil {
    
    static BASE64Decoder decoder = new BASE64Decoder();
    static BASE64Encoder encoder = new BASE64Encoder();
    
    /**
     * 将PDF转换成base64编码
     * 1.使用BufferedInputStream和FileInputStream从File指定的文件中读取内容；
     * 2.然后建立写入到ByteArrayOutputStream底层输出流对象的缓冲输出流BufferedOutputStream
     * 3.底层输出流转换成字节数组，然后由BASE64Encoder的对象对流进行编码
     *
     * @param filePath
     */
    public static String getPDFBinary(String filePath) {
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout = null;
        try {
            //建立读取文件的文件输出流
            fin = new FileInputStream(new File(filePath));
            //在文件输出流上安装节点流（更大效率读取）
            bin = new BufferedInputStream(fin);
            // 创建一个新的 byte 数组输出流，它具有指定大小的缓冲区容量
            baos = new ByteArrayOutputStream();
            //创建一个新的缓冲输出流，以将数据写入指定的底层输出流
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while (len != -1) {
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
            bout.flush();
            byte[] bytes = baos.toByteArray();
            //sun公司的API
            return encoder.encodeBuffer(bytes).trim();
            //apache公司的API
            //return Base64.encodeBase64String(bytes);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
                bin.close();
                //关闭 ByteArrayOutputStream 无效。此类中的方法在关闭此流后仍可被调用，而不会产生任何 IOException
                //baos.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * 将base64编码转换成PDF，保存到
     * 1.使用BASE64Decoder对编码的字符串解码成字节数组
     * 2.使用底层输入流ByteArrayInputStream对象从字节数组中获取数据；
     * 3.建立从底层输入流中读取数据的BufferedInputStream缓冲输出流对象；
     * 4.使用BufferedOutputStream和FileOutputSteam输出数据到指定的文件中
     *
     * @param filePath      文件全路径
     * @param base64sString 文件流
     */
    public static void base64StringToPDF(String base64sString, String filePath) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            byte[] bytes = decoder.decodeBuffer(base64sString);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteArrayInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            
            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                bos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
}
