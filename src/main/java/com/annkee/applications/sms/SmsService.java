package com.annkee.applications.sms;

import com.annkee.applications.weather.WeatherService;
import com.annkee.base.util.HttpClientUtil;
import com.annkee.base.util.ResultVoUtil;
import com.annkee.base.vo.ResultVO;

import java.util.HashMap;

/**
 * 短信发送
 *
 * @author wangan
 * @date 2018/1/10
 */
public class SmsService {
    
    /**
     * 短信推送
     *
     * @return ResultVO
     */
    public ResultVO pushMessage(String cityName) throws Exception {
        
        //被推送的信息
        String data = WeatherService.getData(cityName);
        
        HashMap<String, Object> header = new HashMap<String, Object>(2);
        HashMap<String, Object> param = new HashMap<String, Object>(10);
        
        header.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        param.put("Uid", "test");
        param.put("Key", "test");
        param.put("smsMob", "12345678911");
        param.put("smsText", data);
        
        //中国网建短信平台
        String result = HttpClientUtil.postKeyValue("http://gbk.api.smschinese.cn", header, param);
        return ResultVoUtil.success(result);
    }
}
