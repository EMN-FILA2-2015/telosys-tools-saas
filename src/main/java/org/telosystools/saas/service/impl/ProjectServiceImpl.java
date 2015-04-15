package org.telosystools.saas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telosystools.saas.dao.ProjectDao;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.service.ProjectService;
import org.telosystools.saas.service.WorkspaceService;

import java.util.ArrayList;
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


    @Override
    public List<Project> list() {
        List<Project> projects = new ArrayList<>();
        projectDao.findAll().forEach(e -> projects.add(new Project(e)));

        return projects;
    }

    @Override
    public Project loadProject(String id) {
        return projectDao.load(id);
    }

    @Override
    public void delete(String id) {
        projectDao.remove(id);
    }

    @Override
    public Project createProject(Project project) {
        projectDao.save(project, project.getName());
        workspaceService.createWorkspace(project.getName());
        return project;
    }

}
