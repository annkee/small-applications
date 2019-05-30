package com.annkee.common.util.validatecode;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author wangan
 * @date 2019/1/21
 */
public class ValidateCodeUtil {
    
    /**
     * 获取验证码
     *
     * @return
     * @throws IOException
     */
    public static void getVCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        VCodeGenerator vcg = new VCodeGenerator();
        String vcode = vcg.generatorVCode();
        request.getSession().setAttribute("vcode", vcode);
        BufferedImage vcodeImg = vcg.generatorVCodeImage(vcode, true);
        ImageIO.write(vcodeImg, "gif", response.getOutputStream());
    }
    
    public static void validate(@RequestBody JSONObject json, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        String sessionVcode = (String) request.getSession().getAttribute("vcode");
        String vcode = json.getString("vcode");
        
        JSONObject result = new JSONObject();
        
        if (!sessionVcode.equalsIgnoreCase(vcode)) {
            result.put("msg", "验证码错误");
        } else {
            result.put("msg", "success");
        }
        try {
            response.getWriter().write(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
