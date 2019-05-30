package com.annkee.common.model;

import lombok.Data;

import java.util.Date;

/**
 * @author wangan
 */
@Data
public class LogRecord {
    
    private static final long serialVersionUID = -772076283367705081L;
    
    private Long id;
    
    private String requestContextPath;
    
    private String requestIp;
    
    private String requestMethod;
    
    private String requestQueryString;
    
    private String requestSession;
    
    private String requestUri;
    
    private Integer responseStatus;
    
    private String responseString;
    
    private Date createTime;
    
    private String args;
    
    private String classMethod;
    
    private String params;
    
    public LogRecord(Long id, String requestContextPath, String requestIp, String requestMethod, String requestQueryString, String requestSession, String requestUri, Integer responseStatus, String responseString, Date createTime, String args, String classMethod, String params) {
        this.id = id;
        this.requestContextPath = requestContextPath;
        this.requestIp = requestIp;
        this.requestMethod = requestMethod;
        this.requestQueryString = requestQueryString;
        this.requestSession = requestSession;
        this.requestUri = requestUri;
        this.responseStatus = responseStatus;
        this.responseString = responseString;
        this.createTime = createTime;
        this.args = args;
        this.classMethod = classMethod;
        this.params = params;
    }
    
    public LogRecord() {
        super();
    }
    
}