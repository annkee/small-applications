package com.annkee.applications.dao;

import com.annkee.common.model.LogRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author wangan
 * @date 2019/1/21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LogRecordMapperTest {
    
    @Resource
    LogRecordMapper logRecordMapper;
    
    @Test
    public void insert(){
        LogRecord logRecord = new LogRecord();
        logRecord.setRequestIp("192.168.1.50");
        logRecord.setResponseStatus(0);
        logRecordMapper.insertSelective(logRecord);
    }
}