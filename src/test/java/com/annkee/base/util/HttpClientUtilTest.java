package com.annkee.base.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * http测试
 *
 * @author wangan
 * @date 2018/2/8
 */
public class HttpClientUtilTest {

    @Test
    public void get() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("APP_ID_4", "QA_SERVER:DJkZm2YTZyZDA0");

//        String result = HttpClientUtil.get("http://192.168.8.20/zuul/smartmall-visitor-micro/api/visitor/getVisitorInfo?id=1", map);

    }

    @Test
    public void postJsonObject() {

    }

    @Test
    public void postKeyValue() {
    }
}