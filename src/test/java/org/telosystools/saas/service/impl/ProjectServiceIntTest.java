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
import org.telosystools.saas.domain.ProjectConfiguration;
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
    public static final String CONFIG_FOLDER_ITEM = "folder_1";
    public static final String CONFIG_PACKAGE_ITEM = "packages_1";
    public static final String CONFIG_VARIABLES_ITEM = "variables_1";
    public static final String CONFIG_FOLDER_VALUE = "folder_test";
    public static final String CONFIG_PACKAGES_VALUE = "packages_test";
    public static final String CONFIG_VARIABLES_VALUE = "variables_test";

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
        User user = repUser.findOne(USER_DEFAULT);
        user.getContributions().clear();
        repUser.save(user);
    }

    @Test
    public void testLoadProject() {
        Project expected = new Project();
        expected.setName(PROJECT_NAME);
        expected.setOwner(USER_DEFAULT);
        expected.setDescription("description");

        expected.setProjectConfiguration(new ProjectConfiguration());
        expected = repProject.save(expected);
        IDS.add(expected.getId());

        Project actual = projectService.loadProject(expected.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertNotNull(actual.getProjectConfiguration().getFolders());
        assertNotNull(actual.getProjectConfiguration().getPackages());
        assertNotNull(actual.getProjectConfiguration().getVariables());
    }

    @Test
    public void testCreateProject() {
        Project expected = new Project();
        expected.setName(PROJECT_NAME);
        expected = projectService.createProject(expected);
        IDS.add(expected.getId());

        assertNotNull(expected.getId());
        assertEquals(expected.getOwner(), USER_DEFAULT);
        assertNotNull(projectService.findAllByUser());
        assertNotNull(workService.getWorkspace(expected.getId()));
        workService.deleteWorkspace(expected.getId());

        expected = new Project();
        expected.setName(PROJECT_NAME);
        assertNull(projectService.createProject(expected));

    }

    @Test
    public void testDeleteProject() {
        Project project = new Project();
        project.setName(PROJECT_NAME);
        project.setOwner(USER_DEFAULT);
        project = repProject.save(project);

        projectService.deleteProject(project.getId());
        assertNull(repProject.findOne(project.getId()));
        assertNull(workService.getWorkspace(project.getId()));
    }

    @Test
    public void testFindAllByUser() {
        Project project = new Project();
        project.setName(PROJECT_NAME);
        project.setOwner(USER_DEFAULT);
        project = repProject.save(project);
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

    @Test
    public void testUpdateProjectConfig() {
        ProjectConfiguration config = new ProjectConfiguration();
        config.getFolders().put(CONFIG_FOLDER_ITEM, CONFIG_FOLDER_VALUE);
        config.getPackages().put(CONFIG_PACKAGE_ITEM, CONFIG_PACKAGES_VALUE);
        config.getVariables().put(CONFIG_VARIABLES_ITEM, CONFIG_VARIABLES_VALUE);

        Project expected = new Project();
        expected.setName(PROJECT_NAME);
        expected.setOwner(USER_DEFAULT);
        expected = repProject.save(expected);
        IDS.add(expected.getId());

        projectService.updateProjectConfig(expected.getId(), config);

        Project actual = repProject.findOne(expected.getId());
        assertNotNull(actual);
        assertEquals(CONFIG_FOLDER_VALUE, actual.getProjectConfiguration().getFolders().get(CONFIG_FOLDER_ITEM));
        assertEquals(CONFIG_PACKAGES_VALUE, actual.getProjectConfiguration().getPackages().get(CONFIG_PACKAGE_ITEM));
        assertEquals(CONFIG_VARIABLES_VALUE, actual.getProjectConfiguration().getVariables().get(CONFIG_VARIABLES_ITEM));
    }
}