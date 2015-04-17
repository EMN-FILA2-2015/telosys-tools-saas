package org.telosystools.saas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telosystools.saas.dao.ProjectDao;
import org.telosystools.saas.dao.UserDao;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.domain.ProjectConfiguration;
import org.telosystools.saas.domain.User;
import org.telosystools.saas.service.ProjectService;
import org.telosystools.saas.service.WorkspaceService;
import org.telosystools.saas.web.SecurityUtils;

import java.util.List;

/**
 * Created by Adrian on 29/01/15.
 */
@Component
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private UserDao userDao;


    @Override
    public List<Project> list() {

        return projectDao.findAll();
    }

    @Override
    public List<Project> listByUser() {
        return projectDao.findByUser(SecurityUtils.getCurrentLogin());
    }


    @Override
    public Project loadProject(String projectId) {
        return projectDao.load(projectId);
    }

    @Override
    public void delete(String id) {
        projectDao.remove(id);
    }

    @Override
    public Project createProject(Project project, String userId) {
        // Vérification unicité du nom vis à vis du projet
        User user = userDao.findById(userId);

        projectDao.save(project);
//        user.getProjects().put(project.getId(), "owner");
        userDao.save(user);
        workspaceService.createWorkspace(project.getId());
        return project;
    }

    @Override
    public void updateProjectConfig(String projectId, ProjectConfiguration projectConfig) {
        Project project = projectDao.load(projectId);
        if (project != null) {
            project.setProjectConfiguration(projectConfig);
            projectDao.save(project);
        }
    }

    public User loadUser(String email, String password) {
        return userDao.findByLogin(email, password);
    }

}
