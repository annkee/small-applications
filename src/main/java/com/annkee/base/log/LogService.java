package com.annkee.base.log;

import com.alibaba.fastjson.JSONObject;
import com.annkee.applications.dao.LogRecordMapper;
import com.annkee.applications.domain.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author wangan
 */
@Service
@Slf4j
public class LogService {
    
    @Resource
    private LogRecordMapper logRecordMapper;
    
    @RabbitListener(queues = "logRecord")
    public void onMessage(@Payload String logRecord) {
        log.info("RabbitListener insert log: date={}, log={}", new Date(), logRecord);
        LogRecord logRecordModel = JSONObject.parseObject(logRecord, LogRecord.class);
        logRecordMapper.insertSelective(logRecordModel);
    }
}
