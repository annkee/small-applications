package com.annkee.applications.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.annkee.common.cache.CacheListener;
import com.annkee.common.cache.CacheManagerServiceImpl;
import com.annkee.common.cache.IpUtils;
import com.annkee.common.constant.ConfigFileProperty;
import com.annkee.common.enums.ResultCodeEnum;
import com.annkee.common.util.CommonUtil;
import com.annkee.common.util.ResultVoUtil;
import com.annkee.common.util.validatecode.ValidateCodeUtil;
import com.annkee.common.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 集成图形验证码，同一个ip下的多次请求需要输入图形验证码
 * 10分钟短信有效，点击2次发送验证码之后需要输入图形验证码进行校验
 *
 * @author wangan
 * @date 2018/10/9
 */
@Slf4j
@RestController
@RequestMapping("/sms")
public class AliyunMessageController {
    
    @Autowired
    private ConfigFileProperty configFileProperty;
    
    @Autowired
    private CacheManagerServiceImpl cacheManagerServiceImpl;
    
    /**
     * 产品，固定
     */
    private final String product = "Dysmsapi";
    
    /**
     * 产品域名,开发者无需替换
     */
    private final String domain = "dysmsapi.aliyuncs.com";
    
    private int i = 0;
    
    /**
     * 发送短信
     *
     * @param phoneNumber
     * @param useType     短信使用场景 1验证码 2密码通知
     * @param request
     * @param pwd
     * @param response
     * @return
     */
    @GetMapping(value = "/getMessage")
    public ResultVO sendMsg(@RequestParam("phone") String phoneNumber, @RequestParam("useType") String useType,
                            @RequestParam(value = "pwd", required = false) String pwd,
                            HttpServletRequest request, HttpServletResponse response) {
        CacheListener cacheListener = new CacheListener(cacheManagerServiceImpl);
        cacheListener.startListen();
        
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        
        if (hour > 0 && hour < 7) {
            i++;
            if (i > 150) {
                return ResultVoUtil.error(ResultCodeEnum.SystemNotServe);
            }
        } else {
            i = 0;
        }
        
        synchronized (cacheManagerServiceImpl) {
            
            String KEY = configFileProperty.getSalt();
            
            // 生成随机数
            String randomNum = CommonUtil.createRandomNum(6);
            
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar c = Calendar.getInstance();
            
            
            // 生成10分钟后时间，用户校验是否过期
            c.add(Calendar.MINUTE, 10);
            String currentTime = sf.format(c.getTime());
            String ip = IpUtils.getIpFromRequest(request);
            
            log.warn("ip:{}", ip);
            //如果黑名单存在此IP，给提示
            if (cacheManagerServiceImpl.getCacheByKey("blackIp_" + ip) == null) {
                if (cacheManagerServiceImpl.getCacheByKey("visitorIp_" + ip) == null) {
                    
                    cacheManagerServiceImpl.putCache("visitorIp_", ip, 24 * 60 * 60 * 1000L);
                    
                    if (cacheManagerServiceImpl.getCacheByKey("lastNum_" + ip) == null) {
                        
                        //首次设置剩余次数19
                        cacheManagerServiceImpl.putCache("lastNum_" + ip, 19, 24 * 60 * 60 * 1000L);
                        
                    } else {
                        Integer lastNumIp = Integer.valueOf(cacheManagerServiceImpl.getCacheByKey("lastNum_" + ip).getData().toString());
                        cacheManagerServiceImpl.putCache("lastNum_" + ip, lastNumIp - 1, 24 * 60 * 60 * 1000L);
                        
                        //本机ip
                        if (lastNumIp == 0 && !ip.equals("192.168.1.6")) {
                            cacheManagerServiceImpl.putCache("blackIp_" + ip, ip, 0);
                            
                            log.warn("ip:{}被加入黑名单", ip);
                            log.warn("is keys:{}", cacheManagerServiceImpl.isContains("blackIp_" + ip));
                            return ResultVoUtil.error(ResultCodeEnum.BlackIp);
                        }
                    }
                }
            } else {
                return ResultVoUtil.error(ResultCodeEnum.BlackIp);
            }
            
            //一个手机号是否超过20条短信
            if (cacheManagerServiceImpl.getCacheByKey("lastSmsNum_" + phoneNumber) == null) {
                
                //首次设置剩余次数19
                cacheManagerServiceImpl.putCache("lastSmsNum_" + phoneNumber, 19, 24 * 60 * 60 * 1000L);
            } else {
                Integer lastSmsNum = Integer.valueOf(cacheManagerServiceImpl.getCacheByKey("lastSmsNum_" + phoneNumber).getData().toString());
                cacheManagerServiceImpl.putCache("lastSmsNum_" + phoneNumber, lastSmsNum - 1, 24 * 60 * 60 * 1000L);
                if (lastSmsNum == 0) {
                    log.warn("手机号:{}发送验证码今天超过限制次数", phoneNumber);
                    return ResultVoUtil.error(ResultCodeEnum.PhoneRequestTimeOver);
                }
                
            }
            
            if (cacheManagerServiceImpl.getCacheByKey("sms_" + phoneNumber) != null) {
                
                if (cacheManagerServiceImpl.getCacheByKey("internal_" + phoneNumber) != null) {
                    long internal = cacheManagerServiceImpl.getCacheByKey("internal_" + phoneNumber).getTimeOut();
                    long lastRefeshTime = cacheManagerServiceImpl.getCacheByKey("internal_" + phoneNumber).getLastRefreshTime();
                    long now = System.currentTimeMillis();
                    
                    
                    log.warn("发送短信倒计时internal：{},lastRefeshTime: {}, 剩余：{}s", internal, lastRefeshTime, 60 - (now - lastRefeshTime) / 1000);
                    return ResultVoUtil.error(ResultCodeEnum.RequestSoon);
                } else {
                    // 又可以重新发送验证码了，要输入验证码了
                    try {
                        cacheManagerServiceImpl.clearByKey("sms_" + phoneNumber);
                        ValidateCodeUtil.getVCode(request, response);
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                
                // 发送短信之前进行限制，防止频繁刷新
                CacheManagerServiceImpl cacheManagerService = new CacheManagerServiceImpl();
                
                // 缓存短信验证码失效时间
                cacheManagerService.putCache("sms_" + phoneNumber, randomNum, 10 * 60 * 1000L);
                
                // 缓存请求短信的时间间隔
                cacheManagerService.putCache("internal_" + phoneNumber, randomNum, 60 * 1000L);
                
                CacheListener cacheListenerThis = new CacheListener(cacheManagerService);
                cacheListenerThis.startListen();
                
                // 此处执行发送短信验证码方法
                sendSms(phoneNumber, randomNum, useType, pwd);
                log.warn("执行了发送短信=======");
            }
            
            // 生成MD5值
            String hash = CommonUtil.encrypt(KEY + "@" + currentTime + "@" + randomNum, "md5");
            Map<String, Object> resultMap = new HashMap<>(5);
            resultMap.put("hash", hash);
            resultMap.put("tamp", currentTime);
            resultMap.put("randomNum", randomNum);
            
            log.warn("验证码：{}，返回结果：{}", randomNum, resultMap);
            // 将hash值和tamp时间返回给前端
            return ResultVoUtil.success(resultMap);
        }
    }
    
    /**
     * 手机验证码登陆,相当于是绑定用户信息
     *
     * @param requestMap
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultVO validateNum(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        String KEY = configFileProperty.getSalt();
        
        String requestHash = requestMap.get("hash").toString();
        String tamp = requestMap.get("tamp").toString();
        String msgNum = requestMap.get("msgNum").toString();
        
        String phone = requestMap.get("phone").toString();
        
        //根据手机号去查询用户，不存在则添加
        
        String hash = CommonUtil.encrypt(KEY + "@" + tamp + "@" + msgNum, "md5");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar c = Calendar.getInstance();
        String nowTamp = sf.format(c.getTime());
        
        if (tamp.compareTo(nowTamp) > 0) {
            if (hash.equalsIgnoreCase(requestHash)) {
                //校验成功
                log.warn("校验成功");
                
                return ResultVoUtil.success("校验成功");
            } else {
                //验证码不正确，校验失败
                log.warn("校验失败");
                return ResultVoUtil.error(ResultCodeEnum.ValidateFailedException);
            }
        } else {
            // 超时
            log.warn("校验超时");
            return ResultVoUtil.error(ResultCodeEnum.ValidateTimeoutException);
        }
    }
    
    public SendSmsResponse sendSms(String phoneNumber, String randomNum, String useType, String pwd) {
        
        //此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
        final String accessKeyId = configFileProperty.getAccessKeyId();
        final String accessKeySecret = configFileProperty.getAccessKeySecret();
        
        //模板中的变量替换JSON串：您的验证码${code}，该验证码10分钟内有效，请勿泄漏于他人！
        JSONObject templateParam = new JSONObject();
        
        
        //自定义的短信模板编码，在阿里云短信平台中心配置
        String templateCode = "";
        String signName = "";
        
        if (StringUtils.isEmpty(pwd)) {
            //自己的验证码
            templateParam.put("code", randomNum);
        } else {
            //传给我的密码或什么码
            templateParam.put("code", pwd);
        }
        
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        //初始化acsClient,暂不支持region化
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        
        IClientProfile profile = DefaultProfile.getProfile("cn-beijing", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-beijing", "cn-beijing", product, domain);
        } catch (ClientException e) {
            log.error("sendNoticeSMS error");
        }
        
        //组装请求对象-具体描述见控制台-文档部分内容
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        
        log.warn("templateParam:{}", templateParam);
        request.setTemplateParam(templateParam.toString());
        
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("111");
        
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.warn("阿里巴巴短信返回结果:{}", JSONObject.toJSONString(sendSmsResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}