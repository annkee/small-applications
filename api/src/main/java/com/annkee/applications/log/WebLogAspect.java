package com.annkee.applications.log;

import com.alibaba.fastjson.JSONObject;
import com.annkee.common.model.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author wangan
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
    
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 定义一个切入点
     * 第一个 * 代表任意修饰符及任意返回值.
     * 第二个 * 任意包名
     * 第三个 * 代表任意方法.
     * 第四个 * 定义在web包或者子包
     * 第五个 * 任意方法
     * .. 匹配任意数量的参数
     */
    @Pointcut("execution(* com.annkee.applications.controller..*.*(..))")
    public void webLog() {
    }
    
    /**
     * 切片方式配置log日志记录
     *
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        log.warn("WebLogAspect.doBefore()");
        startTime.set(System.currentTimeMillis());
        // 拿到request和response
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        
        // 获取所有参数
        Enumeration<String> enu = request.getParameterNames();
        StringBuffer sb = new StringBuffer();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            sb.append(request.getParameter(paraName));
        }
        LogRecord logRecord = new LogRecord();
        logRecord.setRequestSession(request.getRequestURL().toString());
        logRecord.setRequestUri(request.getRequestURI());
        logRecord.setRequestMethod(request.getMethod());
        logRecord.setRequestIp(request.getRemoteAddr());
        logRecord.setRequestContextPath(request.getContextPath());
        logRecord.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logRecord.setArgs(Arrays.toString(joinPoint.getArgs()));
        logRecord.setParams(sb.toString());
        logRecord.setCreateTime(new Date());
        // response
        logRecord.setResponseStatus(response.getStatus());
        String mqToDada = JSONObject.toJSONString(logRecord);
        log.warn("logRecord:{}", mqToDada);
        
        rabbitTemplate.convertAndSend("logRecord", mqToDada);
    }
    
    @AfterReturning("webLog()")
    public void doAfterReturning(JoinPoint joinPoint) {
        // 处理完请求，返回内容,如果请求中出现异常不走此方法
        log.warn("WebLogAspect.doAfterReturning() 耗时(毫秒):{}", (System.currentTimeMillis() - startTime.get()));
    }
}
