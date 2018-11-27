package com.annkee.applications.dao;

import com.annkee.applications.domain.Project;

/**
 * @author wangan
 * @date 2018/11/16
 */
public interface ProjectMapper {
    
    /**
     * 查询
     *
     * @param name
     * @return
     */
    Project selectByName(String name);
    
}