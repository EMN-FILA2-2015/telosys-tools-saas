package org.telosystools.saas.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telosystools.saas.domain.project.Project;
import org.telosystools.saas.domain.project.ProjectConfiguration;
import org.telosystools.saas.exception.DuplicateProjectNameException;
import org.telosystools.saas.exception.ProjectNotFoundException;
import org.telosystools.saas.exception.UserNotFoundException;
import org.telosystools.saas.service.ProjectService;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Adrian
 *
 * Controlleur principal
 */

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Inject
    private ProjectService projectService;

    /**
     * Load a project
     * @param id project id
     * @return the Project
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Project> getProject(@PathVariable("id") String id) {
        try {
            Project project = projectService.loadProject(id);
            return new ResponseEntity<>(project,HttpStatus.OK);
        } catch (ProjectNotFoundException e) {
            return new ResponseEntity<>(getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a list of all projects.
     *
     * @return the project list
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            return new ResponseEntity<>(projectService.findAllByUser(),HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(getErrorHttpHeaders(e), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Create a project
     * @param project The Project to create
     * @return The project created
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Project> createProject(@RequestBody Project project) {
        try {
            return new ResponseEntity<>(projectService.createProject(project), HttpStatus.CREATED);
        } catch (DuplicateProjectNameException e) {
            return new ResponseEntity<>(getErrorHttpHeaders(e), HttpStatus.CONFLICT);
        }
    }

    /**
     * Delete a project
     * @param id Project id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@PathVariable("id") String id) {
        // TODO : Exception handling
        projectService.deleteProject(id);
    }


    /**
     * Update the project's configuration.
     *  @param projectId Project ID
     * @param projectConfig Project configuration
     */
    @RequestMapping(value = "/{id}/config/telosystoolscfg", method = RequestMethod.POST)
    public ResponseEntity<Object> setProjectConfig(@PathVariable("id") String projectId, @RequestBody ProjectConfiguration projectConfig) {
        try {
            projectService.updateProjectConfig(projectId, projectConfig);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProjectNotFoundException e) {
            return new ResponseEntity<>(getErrorHttpHeaders(e),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get the project's configuration.
     *
     * @param projectId Project ID
     * @return the Project configuration
     */
    @RequestMapping(value = "/{id}/config/telosystoolscfg", method = RequestMethod.GET)
    public ResponseEntity<ProjectConfiguration> getProjectConfiguration(@PathVariable("id") String projectId) {
        try {
            return new ResponseEntity<>(projectService.loadProject(projectId).getProjectConfiguration(), HttpStatus.OK);
        } catch (ProjectNotFoundException e) {
            return new ResponseEntity<>(getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        }
    }

    private HttpHeaders getErrorHttpHeaders(Exception e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("error_message", e.getMessage());
        return responseHeaders;
    }
}

