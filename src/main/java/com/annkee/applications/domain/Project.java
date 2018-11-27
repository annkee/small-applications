package com.annkee.applications.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangan
 */
@Data
public class Project implements Serializable {
    
    private static final long serialVersionUID = 4879808694747644346L;
    
    private Long id;

    private Integer status;

    private String name;

    private String path;

    private String comment;

    private Integer available;

    private Date updateTime;

    public Project(Long id, Integer status, String name, String path, String comment, Integer available, Date updateTime) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.path = path;
        this.comment = comment;
        this.available = available;
        this.updateTime = updateTime;
    }

    public Project() {
        super();
    }
}