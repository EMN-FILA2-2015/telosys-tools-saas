package org.telosystools.saas.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.telosystools.saas.Application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ProjectControllerTest {

    @Autowired
    private ProjectController projectController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    //@Ignore
    public void testProject() throws Exception {
        this.mockMvc.perform(post("/projects/").contentType(MediaType.APPLICATION_JSON).content("{'name':'telosys'}"))
                .andExpect(status().isCreated());

        this.mockMvc.perform(get("/projects/")).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
    }
}