package com.annkee.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 身份证解析
 *
 * @author wangan
 */
public class IdCardNoUtil {
    
    /**
     * 获取年龄
     *
     * @param idCardNo
     * @return
     */
    public static int getAgeByIdCardNo(String idCardNo) {
        Integer age = -1;
        
        if (idCardNo.length() != 18) {
            return -1;
        }
        
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String birthYear = idCardNo.substring(6, 10);
        String birthMonth = idCardNo.substring(10, 12);
        String birthDay = idCardNo.substring(12, 14);
        String nowDateStr = df.format(new Date());
        
        // 当前年份
        String nowYear = nowDateStr.substring(0, 4);
        // 月份
        String nowMonth = nowDateStr.substring(4, 6);
        String nowDay = nowDateStr.substring(6, 8);
        age = Integer.parseInt(nowYear) - Integer.parseInt(birthYear);
        if (Integer.parseInt(nowMonth) < Integer.parseInt(birthMonth)) {
            return age;
        } else if (Integer.parseInt(nowMonth) > Integer.parseInt(birthMonth)) {
            return age + 1;
        } else {
            if (Integer.parseInt(nowDay) >= Integer.parseInt(birthDay)) {
                return age + 1;
            } else {
                return age;
            }
        }
        
    }
    
    /**
     * 获取性别
     *
     * @param idCard
     * @return
     */
    public static int getSexByIdCardNo(String idCard) {
        Integer sex = -1;
        
        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            //男
            sex = 1;
        } else {
            //女
            sex = 0;
        }
        return sex;
    }
    
}
