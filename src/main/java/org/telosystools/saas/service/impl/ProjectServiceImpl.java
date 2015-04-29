package org.telosystools.saas.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telosystools.saas.dao.ProjectRepository;
import org.telosystools.saas.dao.UserRepository;
import org.telosystools.saas.domain.User;
import org.telosystools.saas.domain.project.Project;
import org.telosystools.saas.domain.project.ProjectConfiguration;
import org.telosystools.saas.exception.DuplicateProjectNameException;
import org.telosystools.saas.exception.ProjectNotFoundException;
import org.telosystools.saas.exception.UserNotFoundException;
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
    public List<Project> findAllByUser() throws UserNotFoundException {
        String login = getCurrentLogin();
        User currentUser = userRepository.findOne(login);
        if (currentUser != null) {
            List<Project> projects = projectRepository.findByOwner(login);

            if (!currentUser.getContributions().isEmpty()) {
                // Récupération des contributions
                final Iterable<Project> contributions = projectRepository.findAll(currentUser.getContributions());
                contributions.forEach(projects::add);
            }

            return projects;
        } else {
            throw new UserNotFoundException(login);
        }
    }

    @Override
    public Project loadProject(String projectId) throws ProjectNotFoundException {
        Project project = projectRepository.findOne(projectId);
        if (project != null) {
            return project;
        } else {
            throw new ProjectNotFoundException(projectId);
        }
    }

    @Override
    public void deleteProject(String projectId) {
        workspaceService.deleteWorkspace(projectId);
        // TODO : Delete all users contributions
        projectRepository.delete(projectId);
    }

    @Override
    public Project createProject(Project project) throws DuplicateProjectNameException {
        // Vérification unicité du nom
        if (!projectRepository.findByOwnerAndName(getCurrentLogin(), project.getName()).isEmpty()) {
            logger.warn("Duplicate project name");
            throw new DuplicateProjectNameException(project.getName());
        }
        project.setOwner(getCurrentLogin());
        project.setProjectConfiguration(new ProjectConfiguration());
        // Création du projet et du workspace
        projectRepository.save(project);
        workspaceService.createWorkspace(project.getId());

        return project;
    }

    @Override
    public void updateProjectConfig(String projectId, ProjectConfiguration projectConfig) throws ProjectNotFoundException {
        Project project = projectRepository.findOne(projectId);
        if (project != null) {
            project.setProjectConfiguration(projectConfig);
            projectRepository.save(project);
        } else {
            throw new ProjectNotFoundException(projectId);
        }
    }

}
