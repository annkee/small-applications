package com.annkee.base.util;

import com.annkee.base.constant.ProjectConstant;
import com.annkee.base.enums.ResultCodeEnum;
import com.annkee.base.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;

/**
 * 工具类
 *
 * @author wangan
 * @date 2018/10/10
 */
@Slf4j
public class CommonUtil {
    
    /**
     * 密钥规则
     */
    private static final String RULE = "JH_INS_STUDENT_S";
    /**
     * 偏移量
     */
    private static final String IVPARAMETER = "10awe90a905e3400";
    
    private static final String UTF8 = "UTF-8";
    private static final String SHA256 = "SHA-256";
    
    /**
     * 随机字符串
     *
     * @return
     */
    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 生成随机数
     *
     * @param num 位数
     * @return
     */
    public static String createRandomNum(int num) {
        String randomNumStr = "";
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            int randomNum = random.nextInt(10);
            if (i == 0 && randomNum == 0) {
                randomNum = 1;
            }
            randomNumStr += randomNum;
        }
        return randomNumStr;
    }
    
    /**
     * 订单号生成规则文档
     *
     * @return
     */
    public static synchronized Long genUniqueKey() {
        
        Random random = new Random();
        Integer number = random.nextInt(90000) + 10000;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        
        return Long.valueOf(dateFormat.format(new Date()) + number);
        
    }
    
    /**
     * 对message进行sha1或者MD5的加密
     *
     * @param message
     * @param type    加密类型，sha1或者md5
     * @return
     */
    public static String encrypt(String message, String type) {
        try {
            StringBuffer sb = new StringBuffer();
            MessageDigest digest = MessageDigest.getInstance(type);
            digest.update(message.getBytes());
            byte[] digestMsg = digest.digest();
            for (byte b : digestMsg) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("commonUtil加密异常：{}", e.getMessage());
        }
        return null;
    }
    
    /**
     * aes加密
     *
     * @param str
     * @param encodingFormat
     * @param sKey
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String str, String encodingFormat, String sKey, String ivParameter) {
        
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(str.getBytes(encodingFormat));
            String encode = new BASE64Encoder().encode(encrypted);
            //此处使用BASE64做转码,去掉回车
            return encode.replaceAll(System.getProperty("line.separator"), "");
        } catch (Exception e) {
            log.error("aesEncrypt error:{}", e.getMessage());
            throw new BaseException(ResultCodeEnum.AES_ENCRYPT_ERROR);
        }
    }
    
    /**
     * 解密
     *
     * @param str
     * @param encodingFormat
     * @param sKey
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String str, String encodingFormat, String sKey, String ivParameter) {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            //先用base64解密
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(str);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, encodingFormat);
            return originalString;
        } catch (Exception e) {
            log.error("aesDecrypt error:{}", e.getMessage());
            
            throw new BaseException(ResultCodeEnum.AES_DECRYPT_ERROR);
        }
    }
    
    /**
     * 获取请求头传来的token
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = String.valueOf(headerNames.nextElement());
            log.warn("header keys={}", key);
            
            if (ProjectConstant.AUTHORIZATION.equalsIgnoreCase(key)) {
                String value = request.getHeader(key);
                log.warn("value={}", value);
                return value;
            }
            
        }
        return null;
        
    }
    
    public static String parseDouble2Str(double param) throws ParseException {
        //double保留2位小数，返回string
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(param);
    }
    
    public static double parseDouble2Double(double param) throws ParseException {
        //double保留2位小数，返回double
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(param));
    }
    
    
    /**
     * SHA256加密
     */
    public static String sha256Hex(String data) {
        try {
            return toHexString(sha256(data.getBytes(UTF8)));
        } catch (UnsupportedEncodingException e) {
            log.error("MD5Util SHA256 加密出错", e);
            return null;
        }
    }
    
    private static String toHexString(byte[] b) {
        StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            stringbuffer.append(IVPARAMETER.charAt(b[i] >>> 4 & 0x0F));
            stringbuffer.append(IVPARAMETER.charAt(b[i] & 0x0F));
        }
        return stringbuffer.toString();
    }
    
    /**
     * SHA-256加密，并返回作为一个十六进制字节
     */
    public static byte[] sha256(byte[] data) {
        return getDigestBySha().digest(data);
    }
    
    /**
     * 返回 MessageDigest SHA-256
     */
    private static MessageDigest getDigestBySha() {
        try {
            return MessageDigest.getInstance(SHA256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
}
