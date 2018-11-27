package com.annkee.base.auth;

import com.annkee.applications.domain.model.UserModel;
import com.annkee.applications.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/**
 * 自定义身份认证
 * 基于HMAC（散列消息认证码）的控制域
 *
 * @author wangan
 */
@Slf4j
public class JwtShiroRealm extends AuthorizingRealm {
    
    protected UserService userService;
    
    public JwtShiroRealm(UserService userService) {
        this.userService = userService;
        this.setCredentialsMatcher(new JwtCredentialsMatcher());
    }
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
    
    /**
     * 认证信息.身份验证: Authentication 是用来验证用户身份
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        log.warn("doGetAuthenticationInfo come in");
        JwtToken jwtToken = (JwtToken) authcToken;
        String token = jwtToken.getToken();
        
        UserModel user = userService.getJwtTokenInfo(JwtUtil.getUsername(token));
        if (user == null) {
            throw new AuthenticationException("token过期，请重新登录");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getSalt(), "jwtRealm");
        
        return authenticationInfo;
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }
}
