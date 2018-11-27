package com.annkee.applications.controller;

import com.annkee.base.constant.ProjectConstant;
import com.annkee.base.enums.ResultCodeEnum;
import com.annkee.base.exception.BaseException;
import com.annkee.base.util.HttpClientUtil;
import com.annkee.base.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author wangan
 * @date 2018/5/29
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {
    
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传", notes = "无须注意")
    public ResultVO upload(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        
        if (multipartFile.isEmpty()) {
            throw new BaseException(ResultCodeEnum.PARAM_ERROR);
        }
        
        HashMap<String, Object> headers = new HashMap<>(3);
        headers.put("secret-key", ProjectConstant.SECRET_KEY);
        headers.put("identity-id", ProjectConstant.IDENTITY_ID);
        //上传文件
        return HttpClientUtil.upload(request, ProjectConstant.FINAL_URL, headers, multipartFile);
    }
    
}
