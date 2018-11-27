package com.annkee.applications.service.impl;

import com.annkee.applications.dao.ProjectMapper;
import com.annkee.applications.domain.Project;
import com.annkee.applications.service.ProjectService;
import com.annkee.base.enums.ResultCodeEnum;
import com.annkee.base.exception.BaseException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wangan
 * @date 2018/5/29
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Resource
    private ProjectMapper projectMapper;
    
    @Override
    public Project getProjectByName(String projectName) {
        Project project = projectMapper.selectByName(projectName);
        throw new BaseException(ResultCodeEnum.SQLException);
//        return project;
    }
}
