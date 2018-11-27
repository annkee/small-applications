package com.annkee.base.auth;

import org.apache.shiro.authc.HostAuthenticationToken;

/**
 * @author wangan
 */
public class JwtToken implements HostAuthenticationToken {
    
    private static final long serialVersionUID = 2140302262995998641L;
    private String token;
    private String host;
    
    public JwtToken(String token) {
        this(token, null);
    }
    
    public JwtToken(String token, String host) {
        this.token = token;
        this.host = host;
    }
    
    public String getToken() {
        return this.token;
    }
    
    @Override
    public String getHost() {
        return host;
    }
    
    @Override
    public Object getPrincipal() {
        return token;
    }
    
    @Override
    public Object getCredentials() {
        return token;
    }
    
    @Override
    public String toString() {
        return token + ':' + host;
    }
}
