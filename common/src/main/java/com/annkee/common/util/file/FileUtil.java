package com.annkee.common.util.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件操作工具
 *
 * @author wangan
 * @date 2018/1/19
 */
@Slf4j
public class FileUtil {
    
    private FileUtil() {
    }
    
    
    /**
     * 创建目录
     *
     * @param dirPath 目录名,包含路径example: “d:/testDir”
     * @return boolean 成功 true 失败 false
     */
    
    public static boolean createDirectory(String dirPath) {
        StringBuffer bufferDirPath = new StringBuffer(dirPath);
        
        if (!dirPath.endsWith(File.separator)) {
            bufferDirPath.append(File.separator);
        }
        File file = new File(bufferDirPath.toString());
        
        if (file.mkdirs() == false) {
            log.info("目录已存在");
            return false;
        }
        
        return file.mkdirs();
    }
    
    
    /**
     * 把文件保存到本地
     *
     * @param stream   将文件加载到输入流管道
     * @param path     文件保存路径
     * @param filename 文件全名包括后缀
     * @throws IOException
     */
    
    public static void saveFileFromInputStream(InputStream stream, String path, String filename) throws Exception {
        
        FileOutputStream fs = null;
        
        if (stream != null) {
            fs = new FileOutputStream(path + "/" + filename, true);
            byte[] bytes = new byte[4096];
            int i = 0;
            //-1是内容读完了
            while ((i = stream.read(bytes)) != -1) {
                
                fs.write(bytes, 0, i);
                fs.flush();
                
            }
            fs.close();
        }
        
    }
    
    
    /**
     * 通用下载
     *
     * @param request
     * @param response
     * @param file
     * @param isDel
     * @throws Exception
     */
    
    public static void download(HttpServletRequest request, HttpServletResponse response, File file, boolean isDel) throws Exception {
        
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        long fileLength = file.length();
        response.setContentType("application/octet-stream");
        setFileDownloadHeader(request, file.getName());
        
        //这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
        
        response.setHeader("Content-Disposition", "attachment; filename=\"" +
                setFileDownloadHeader(request, file.getName()) + "\"");
        
        response.setHeader("Content-Length", String.valueOf(fileLength));
        
        bis = new BufferedInputStream(new FileInputStream(file));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            
            bos.write(buff, 0, bytesRead);
        }
        
        bis.close();
        bos.close();
        if (file.exists() && isDel) {
            file.delete();
            
        }
        
    }
    
    /**
     * 解决导出文件名乱码问题
     *
     * @param request
     * @param fileName
     */
    
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) {
        
        final String userAgent = request.getHeader("USER-AGENT");
        String finalFileName = null;
        try {
            //IE浏览器
            if (userAgent.contains("MSIE")) {
                
                finalFileName = URLEncoder.encode(fileName, "UTF8");
            } else if (userAgent.contains("Mozilla")) {
                
                //google,火狐浏览器
                finalFileName = new String(fileName.getBytes(), "ISO8859-1");
                
            } else {
                //其他浏览器
                finalFileName = URLEncoder.encode(fileName, "UTF8");
                
            }
            
            //response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
            
        } catch (UnsupportedEncodingException e) {
        
        }
        
        return finalFileName;
        
    }
    
    /**
     * 读取内容
     *
     * @param in
     * @return
     */
    
    public static List<String> loadDate(InputStream in) {
        
        List<String> res0 = new ArrayList<>();
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        try {
            
            isr = new InputStreamReader(in, "gbk");
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                
                res0.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    
                    e.printStackTrace();
                }
            }
        }
        
        return res0;
    }
    
    
    public static void saveImg(InputStream inStream, String path) {
        
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        
        byte[] data;
        FileOutputStream outStream = null;
        try {
            
            data = readInputStream(inStream);
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            File imageFile = new File(path);
            //创建输出流
            outStream = new FileOutputStream(imageFile);
            //写入数据
            outStream.write(data);
            //关闭输出流
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    
                }
                
            }
            
        }
    }
    
    
    private static byte[] readInputStream(InputStream inStream) throws Exception {
        
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
     * 多文件上传
     * 返回一个多文件上传后的文件名地址的List 使用springMVC提供的CommonsMultipartFile类进行读取文件
     *
     * @param files      数组接收文件
     * @param request
     * @param uploadPath 上传路径
     * @return
     */
    
    public static List<String> manyFileUpload(CommonsMultipartFile[] files, HttpServletRequest request, String
            uploadPath) {
        
        //定义一个List，用于存放各文件的路径，返回
        
        List<String> list = new ArrayList<String>();
        //new 一个路径文件
        File f = new File(uploadPath);
        
        //如果路径不存在，新建路径
        if (!f.exists()) {
            
            f.mkdirs();
        }
        
        for (int i = 0; i < files.length; i++) {
            
            // 获得原始文件名
            String fileName = files[i].getOriginalFilename();
            System.out.println("原始文件名:" + fileName);
            // 新文件名
            String newFileName = UUID.randomUUID() + fileName;
            
            if (!files[i].isEmpty()) {
                
                try {
                    FileOutputStream fos = new FileOutputStream(uploadPath + newFileName);
                    InputStream in = files[i].getInputStream();
                    int b = 0;
                    while ((b = in.read()) != -1) {
                        
                        fos.write(b);
                    }
                    fos.close();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("上传图片到:" + uploadPath + newFileName);
            list.add(uploadPath + newFileName);
        }
        return list;
    }
    
}

