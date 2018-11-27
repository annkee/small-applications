package com.annkee.applications.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户对象
 *
 * @author wangan
 */
@Data
public class UserModel implements Serializable {
    
    private static final long serialVersionUID = -4542936384221999180L;
    
    private String username;
    private char[] password;
    private String encryptPwd;
    private Long userId;
    private String salt;
    private List<String> roles;
}
