package org.telosystools.saas.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telosystools.saas.dao.ProjectRepository;
import org.telosystools.saas.dao.UserRepository;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.domain.ProjectConfiguration;
import org.telosystools.saas.domain.User;
import org.telosystools.saas.service.ProjectService;
import org.telosystools.saas.service.WorkspaceService;

import java.util.List;

import static org.telosystools.saas.web.SecurityUtils.getCurrentLogin;

/**
 * Created by Adrian on 29/01/15.
 *
 * Project management service.
 * Uses the workspace services as a workspace is always attached to a project
 */
@Component
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Override
    public List<Project> findAllByUser() {
        // TODO : Exception UserNotFound
        User currentUser = userRepository.findOne(getCurrentLogin());
        List<Project> projects = projectRepository.findByOwner(getCurrentLogin());

        if (!currentUser.getContributions().isEmpty()) {
            // Récupération des contributions
            final Iterable<Project> contributions = projectRepository.findAll(currentUser.getContributions());
            contributions.forEach(projects::add);
        }

        return projects;
    }

    @Override
    public Project loadProject(String projectId) {
        return projectRepository.findOne(projectId);
    }

    @Override
    public void deleteProject(String projectId) {
        workspaceService.deleteWorkspace(projectId);
        // TODO : Delete all users contributions
        projectRepository.delete(projectId);
    }

    @Override
    public Project createProject(Project project) {
        // Vérification unicité du nom
        if (!projectRepository.findByOwnerAndName(getCurrentLogin(), project.getName()).isEmpty()) {
            logger.warn("Duplicate project name");
            // TODO : Throw ProjectDuplicateNameException
            return null;
        }
        project.setOwner(getCurrentLogin());

        // Création du projet et du workspace
        projectRepository.save(project);
        workspaceService.createWorkspace(project.getId());

        return project;
    }

    @Override
    public void updateProjectConfig(String projectId, ProjectConfiguration projectConfig) {
        Project project = projectRepository.findOne(projectId);
        // TODO : Throw ProjectNotFound
        if (project != null) {
            project.setProjectConfiguration(projectConfig);
            projectRepository.save(project);
        }
    }

}
