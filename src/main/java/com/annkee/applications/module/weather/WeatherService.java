package com.annkee.applications.module.weather;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * 每天早上获取天气信息
 *
 * @author wangan
 * @date 2018/1/9
 */

@Slf4j
public class WeatherService {
    
    /**
     * 获取天气
     * http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/getSupportCity
     *
     * @return String
     */
    public static String getData(String cityName) throws Exception {
        
        String getUrl = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName?theCityName=";
        
        URL url = new URL(getUrl + cityName);
        URLConnection connectionData = url.openConnection();
        connectionData.setConnectTimeout(80000);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                connectionData.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            
            sb.append(line);
        }
        JSONObject weatherJson = XML.toJSONObject(sb.toString());
        log.info(weatherJson.toString());
        String string = weatherJson.getJSONObject("ArrayOfString").get("string").toString();
        String[] data = string.split(",");
        String wendu = data[5];
        String todayFeng = data[7];
        String weather = data[10];
        String ziwaixian = data[11];
        
        String tomorrowWendu = data[12];
        String tomorrowTianqi = data[13];
        String tomorrowFeng = data[14];
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("<label style='color: #F07216;font-size: 20px'>").append(cityName).append("今天天气：</label>").append("<label style='color: #2D76B6'>").append(wendu).append("<br/>").append(weather).append("</label><br/>")
                .append("<label style='color: #F07216;font-size: 16px'>风：</label>").append("<label style='color: #2D76B6'>").append(todayFeng).append("</label><br/>")
                .append("<label style='color: #F07216;font-size: 16px'>注意：</label>").append("<label style='color: #2D76B6'>").append(ziwaixian).append("</label><br/>")
                .append("<label style='color: #F07216;font-size: 20px'>明天天气：</label>").append("<label style='color: #2D76B6'>").append(tomorrowWendu).append("<br/>")
                .append(tomorrowTianqi).append("</label><br/>")
                .append("<label style='color: #F07216;font-size: 16px'>风：</label>").append("<label style='color: #2D76B6'>").append(tomorrowFeng).append("</label><br/>");
        String message = buffer.toString();
        return message;
        
    }
    
    
}
