package com.annkee.base.auth;

import com.annkee.applications.domain.model.UserModel;
import com.annkee.applications.service.UserService;
import com.annkee.base.constant.ProjectConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.List;

/**
 * @author wangan
 */
@Slf4j
public class UserShiroRealm extends AuthorizingRealm {
    
    private UserService userService;
    
    public UserShiroRealm(UserService userService) {
        this.userService = userService;
        this.setCredentialsMatcher(new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME));
    }
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userPasswordToken = (UsernamePasswordToken) token;
        String username = userPasswordToken.getUsername();
        UserModel user = userService.getUserInfo(username);
        if (user == null) {
            throw new AuthenticationException("用户名或者密码错误");
        }
        return new SimpleAuthenticationInfo(user, user.getEncryptPwd(), ByteSource.Util.bytes(ProjectConstant.JWT_ENCRYPT_SALT), "dbRealm");
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        UserModel user = (UserModel) principals.getPrimaryPrincipal();
        List<String> roles = user.getRoles();
        if (roles == null) {
            roles = userService.getUserRoles(user.getUserId());
            user.setRoles(roles);
        }
        if (roles != null) {
            simpleAuthorizationInfo.addRoles(roles);
        }
        return simpleAuthorizationInfo;
    }
    
    
}
