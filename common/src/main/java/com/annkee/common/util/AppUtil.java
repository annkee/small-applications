package com.annkee.common.util;

import com.annkee.common.enums.ResultCodeEnum;
import com.annkee.common.exception.BaseException;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * app包解析
 *
 * @author wangan
 * @date 2018/7/11
 */
public class AppUtil {
    
    /**
     * 获取APK信息
     *
     * @param apkUrl
     * @return
     */
    public static Map<String, Object> readApk(String apkUrl) {
        Map<String, Object> resMap = new HashMap<String, Object>(5);
        
        try (ApkFile apkFile = new ApkFile(new File(apkUrl))) {
            ApkMeta apkMeta = apkFile.getApkMeta();
            resMap.put("versionCode", apkMeta.getVersionCode());
            resMap.put("versionName", apkMeta.getVersionName());
            resMap.put("packageName", apkMeta.getPackageName());
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(ResultCodeEnum.APP_PARSE_ERROR);
        }
        return resMap;
    }
    
    
    /**
     * 读取ipa
     */
    public static Map<String, Object> readIPA(String ipaURL) {
        Map<String, Object> map = new HashMap<String, Object>(6);
        try {
            File file = new File(ipaURL);
            InputStream is = new FileInputStream(file);
            ZipInputStream zipIns = new ZipInputStream(is);
            ZipEntry ze;
            InputStream infoIs = null;
            NSDictionary rootDict = null;
            while ((ze = zipIns.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    String name = ze.getName();
                    if (null != name &&
                            name.toLowerCase().contains(".app/info.plist")) {
                        ByteArrayOutputStream _copy = new
                                ByteArrayOutputStream();
                        int chunk = 0;
                        byte[] data = new byte[1024];
                        while (-1 != (chunk = zipIns.read(data))) {
                            _copy.write(data, 0, chunk);
                        }
                        infoIs = new ByteArrayInputStream(_copy.toByteArray());
                        rootDict = (NSDictionary) PropertyListParser.parse(infoIs);
                        break;
                    }
                }
            }
            
            // 应用包名
            NSString parameters = (NSString) rootDict.get("CFBundleIdentifier");
            map.put("packageName", parameters.toString());
            // 应用版本名
            parameters = (NSString) rootDict.objectForKey("CFBundleShortVersionString");
            map.put("versionName", parameters.toString());
            //应用版本号
            parameters = (NSString) rootDict.get("CFBundleVersion");
            map.put("versionCode", parameters.intValue());
            
            infoIs.close();
            is.close();
            zipIns.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(ResultCodeEnum.APP_PARSE_ERROR);
        }
        return map;
    }
}
