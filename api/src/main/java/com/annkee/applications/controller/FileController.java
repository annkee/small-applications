package com.annkee.applications.controller;

import com.alibaba.fastjson.JSONObject;
import com.annkee.common.enums.ResultCodeEnum;
import com.annkee.common.util.ResultVoUtil;
import com.annkee.common.util.imagecode.QRCodeUtil;
import com.annkee.common.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangan
 * @date 2019/1/14
 */
@Slf4j
@RestController
@RequestMapping("/annkee/file")
public class FileController {
    
    @Autowired
    private QRCodeUtil qrCodeUtil;
    
    /**
     * 阿里云oss文件上传
     *
     * @param
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/upload")
    public ResultVO uploadFile(@RequestBody JSONObject param, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 二维码链接内容
            String content = param.getString("content");
            // http地址的海报
            String backImg = param.getString("backImg");
            log.warn("二维码生成参数：{}", param);
            String urlPath = qrCodeUtil.encode(content, backImg);
            
            return ResultVoUtil.success(urlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVoUtil.error(ResultCodeEnum.SYSTEM_ERROR);
    }
}
