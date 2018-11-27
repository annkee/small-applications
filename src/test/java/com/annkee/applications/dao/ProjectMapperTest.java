package com.annkee.applications.dao;

import com.annkee.applications.domain.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author wangan
 * @date 2018/11/22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectMapperTest {
    
    @Resource
    ProjectMapper projectMapper;
    
    @Test
    public void selectByPrimaryKey() {
        Project project = projectMapper.selectByName("易问");
    }
}