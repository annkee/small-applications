package com.annkee.applications.service;

import com.annkee.applications.domain.Project;

/**
 * @author wangan
 * @date 2018/5/29
 */
public interface ProjectService {

    /**
     * 查询项目
     *
     * @param projectName
     * @return
     */
    Project getProjectByName(String projectName);

}
