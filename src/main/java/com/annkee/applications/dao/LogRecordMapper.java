package com.annkee.applications.dao;

import com.annkee.applications.domain.LogRecord;

/**
 * @author wangan
 */
public interface LogRecordMapper {
    
    /**
     * 插入日志访问
     *
     * @param record
     * @return
     */
    int insertSelective(LogRecord record);
    
}