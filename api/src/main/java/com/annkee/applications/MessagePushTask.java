package com.annkee.applications;


import com.annkee.applications.mail.MailService;
import com.annkee.applications.weather.WeatherService;
import com.annkee.common.enums.ResultCodeEnum;
import com.annkee.common.util.ResultVoUtil;
import com.annkee.common.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wangan
 * @date 2018/1/10
 */
@Component
@Slf4j
public class MessagePushTask {
    
    /**
     * 定时任务 每天早上6.59推送
     *
     * @return ResultVO
     * @throws Exception
     */
    @Scheduled(cron = "0 59 6 1/1 * ? ")
    public static ResultVO send() throws Exception {
        log.warn("--execute weather push---");
        String beijing = ResultCodeEnum.BEIJING.getMessage();
        String beijingWeather = WeatherService.getData(beijing);
        Thread.sleep(1000);

        log.warn(beijingWeather);
        MailService.sendHtmlMail("wangan058@163.com", "天气预报", beijingWeather);
        log.warn("---success---");
        return ResultVoUtil.success();
    }
}
