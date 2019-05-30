package com.annkee.applications.auth;

import com.annkee.applications.model.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import java.io.UnsupportedEncodingException;

/**
 * @author wangan
 */
@Slf4j
public class JwtCredentialsMatcher implements CredentialsMatcher {
    
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        log.warn("doCredentialsMatch come in");
        String token = (String) authenticationToken.getCredentials();
        Object stored = authenticationInfo.getCredentials();
        String salt = stored.toString();
        
        UserModel user = (UserModel) authenticationInfo.getPrincipals().getPrimaryPrincipal();
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", user.getUsername())
                    .build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException | JWTVerificationException e) {
            log.error("token Error:{}", e.getMessage());
        }
        
        return false;
    }
    
}
