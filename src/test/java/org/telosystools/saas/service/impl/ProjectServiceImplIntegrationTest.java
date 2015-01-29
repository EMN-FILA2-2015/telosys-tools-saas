package org.telosystools.saas.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.telosystools.saas.Application;
import org.telosystools.saas.domain.Project;

import java.util.List;

/**
 * Created by Adrian on 29/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ProjectServiceImplIntegrationTest {

    @Autowired
    private ProjectServiceImpl projectService;

    @Test
    public void testIntegration() {
        assertNotNull(projectService);

        Project telosys;
        Project docker;

        telosys = projectService.insert(new Project("telosys"));
        docker = projectService.insert(new Project("docker"));

        assertNotNull(telosys);
        assertNotNull(docker);
        assertNotNull(telosys.getId());
        assertNotNull(docker.getId());

        List<Project> list = projectService.list();

        assertEquals("2 elements", 2, list.size());
        assertEquals("Name", telosys.getName(), list.get(0).getName());

        Project project = projectService.find(telosys.getId());
        assertEquals("Telosys", telosys.getName(), project.getName());

        projectService.delete(telosys.getId());

        assertNull(projectService.find(telosys.getId()));
        assertEquals("1 element", 1, projectService.list().size());

        projectService.delete(docker.getId());
    }
}
