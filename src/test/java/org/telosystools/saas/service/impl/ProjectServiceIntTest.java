package org.telosystools.saas.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.telosystools.saas.Application;
import org.telosystools.saas.MongodbConfiguration;
import org.telosystools.saas.dao.ProjectRepository;
import org.telosystools.saas.dao.UserRepository;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.domain.User;
import org.telosystools.saas.service.WorkspaceService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Adrian on 29/01/15.
 *
 * Integration tests of ProjectService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Import(MongodbConfiguration.class)
public class ProjectServiceIntTest {

    public static final String USER_DEFAULT = "user_default";
    public static final String PROJECT_NAME = "project-test";
    public static final String OTHER_OWNER = "other_owner";

    @Autowired
    private ProjectServiceImpl projectService;

    private WorkspaceService workService;

    private ProjectRepository repProject;

    private UserRepository repUser;

    private static List<String> IDS = new ArrayList<>();

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        final Field projRepField = ProjectServiceImpl.class.getDeclaredField("projectRepository");
        projRepField.setAccessible(true);
        repProject = (ProjectRepository) projRepField.get(projectService);

        final Field userRepField = ProjectServiceImpl.class.getDeclaredField("userRepository");
        userRepField.setAccessible(true);
        repUser = (UserRepository) userRepField.get(projectService);

        final Field workServiceField = ProjectServiceImpl.class.getDeclaredField("workspaceService");
        workServiceField.setAccessible(true);
        workService = (WorkspaceService) workServiceField.get(projectService);

        if (!repUser.exists(USER_DEFAULT)) {
            User defaultUser = new User(USER_DEFAULT);
            repUser.save(defaultUser);
        }
    }

    @After
    public void tearDown() {
        IDS.forEach(repProject::delete);
    }

    @Test
    public void testCreateProject() {
        Project project = new Project();
        project.setName(PROJECT_NAME);
        project = projectService.createProject(project);

        assertNotNull(project.getId());
        assertEquals(project.getOwner(), USER_DEFAULT);
        assertNotNull(projectService.findAllByUser());

        IDS.add(project.getId());

        project = new Project();
        project.setName(PROJECT_NAME);
        assertNull(projectService.createProject(project));
    }

    @Test
    public void testDeleteProject() {
        Project project = new Project();
        project.setName(PROJECT_NAME);
        project = projectService.createProject(project);

        projectService.deleteProject(project.getId());
        assertNull(repProject.findOne(project.getId()));
        assertNull(workService.getWorkspace(project.getId()));
    }

    @Test
    public void testFindAllByUser() {
        Project project = new Project();
        project.setName(PROJECT_NAME);
        project = projectService.createProject(project);
        IDS.add(project.getId());


        Project otherProject = new Project();
        otherProject.setOwner(OTHER_OWNER);
        otherProject = repProject.save(otherProject);
        IDS.add(otherProject.getId());

        User user = repUser.findOne(USER_DEFAULT);
        user.addContribution(otherProject.getId());
        repUser.save(user);

        List<Project> res = projectService.findAllByUser();
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(project.getId(), res.get(0).getId());
        assertEquals(project.getOwner(), res.get(0).getOwner());
        assertEquals(otherProject.getId(), res.get(1).getId());
        assertEquals(otherProject.getOwner(), OTHER_OWNER);
    }
}