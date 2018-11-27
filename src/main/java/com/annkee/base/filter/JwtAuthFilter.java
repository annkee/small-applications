package com.annkee.base.filter;

import com.alibaba.druid.util.StringUtils;
import com.annkee.applications.domain.model.UserModel;
import com.annkee.applications.service.UserService;
import com.annkee.base.auth.JwtToken;
import com.annkee.base.auth.JwtUtil;
import com.annkee.base.enums.ResultCodeEnum;
import com.annkee.base.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author wangan
 */
@Slf4j
public class JwtAuthFilter extends AuthenticatingFilter {
    
    private static final int tokenRefreshInterval = 300;
    
    private UserService userService;
    
    public JwtAuthFilter(UserService userService) {
        this.userService = userService;
        this.setLoginUrl("/login");
    }
    
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.warn("preHandle come in");
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //对于OPTION请求做拦截，不做token校验
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return false;
        }
        
        return super.preHandle(request, response);
    }
    
    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {
        log.warn("postHandle come in");
        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
        request.setAttribute("jwtShiroFilter.FILTERED", true);
    }
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.warn("isAccessAllowed come in");
        if (this.isLoginRequest(request, response)) {
            return true;
        }
        Boolean afterFiltered = (Boolean) (request.getAttribute("jwtShiroFilter.FILTERED"));
        if (Boolean.TRUE.equals(afterFiltered)) {
            return true;
        }
        
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (Exception e) {
            log.error("Error occurs when login:{}", e.getMessage());
            e.printStackTrace();
            throw new BaseException(ResultCodeEnum.USER_LOGIN_ERROR);
        }
        return allowed || super.isPermissive(mappedValue);
    }
    
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        log.warn("createToken come in");
        String jwtToken = getAuthzHeader(servletRequest);
        if (!StringUtils.isEmpty(jwtToken) && !JwtUtil.isTokenExpired(jwtToken)) {
            return new JwtToken(jwtToken);
        }
        
        return null;
    }
    
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.warn("onAccessDenied come in");
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
        return false;
    }
    
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        log.warn("onLoginSuccess come in");
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if (token instanceof JwtToken) {
            JwtToken jwtToken = (JwtToken) token;
            UserModel user = (UserModel) subject.getPrincipal();
            boolean shouldRefresh = shouldTokenRefresh(JwtUtil.getIssuedAt(jwtToken.getToken()));
            if (shouldRefresh) {
                newToken = userService.generateJwtToken(user.getUsername());
            }
        }
        if (StringUtils.isEmpty(newToken)) {
            httpResponse.setHeader("x-auth-token", newToken);
        }
        return true;
    }
    
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }
    
    protected String getAuthzHeader(ServletRequest request) {
        log.warn("getAuthzHeader come in");
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String header = httpRequest.getHeader("x-auth-token");
        
        return removeStart(header, "Bearer ");
    }
    
    protected boolean shouldTokenRefresh(Date issueAt) {
        log.warn("shouldTokenRefresh come in");
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }
    
    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        log.warn("jwtAuthFilter come in");
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
    }
    
    public static String removeStart(String str, String remove) {
        log.warn("removeStart come in");
        if (!StringUtils.isEmpty(str)) {
            return str.startsWith(remove) ? str.substring(remove.length()) : str;
        } else {
            return str;
        }
    }
}
