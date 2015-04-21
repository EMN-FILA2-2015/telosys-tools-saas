package org.telosystools.saas.web.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.telosystools.saas.Application;
import org.telosystools.saas.config.MongoConfiguration;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.service.ProjectService;

import java.lang.reflect.Field;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Import(MongoConfiguration.class)
public class ProjectControllerTest {

    @Autowired
    private ProjectController projectController;

    private ProjectService service;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
        final Field serviceField = ProjectController.class.getDeclaredField("projectService");
        serviceField.setAccessible(true);
        service = (ProjectService) serviceField.get(projectController);
    }

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException {
        List<Project> projects = service.findAllByUser();
        projects.forEach(e -> service.deleteProject(e.getId()));
    }

    @Test
    public void testProject() throws Exception {
        final MvcResult mvcResultCreate = this.mockMvc.perform(post("/projects/").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"telosys\"}"))
                .andExpect(status().isCreated()).andReturn();
        this.mockMvc.perform(get("/projects/")).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
    }
}