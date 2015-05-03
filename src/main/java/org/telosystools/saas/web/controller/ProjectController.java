package org.telosystools.saas.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telosystools.saas.domain.filesystem.File;
import org.telosystools.saas.domain.filesystem.FileData;
import org.telosystools.saas.domain.filesystem.Workspace;
import org.telosystools.saas.domain.project.Project;
import org.telosystools.saas.domain.project.ProjectConfiguration;
import org.telosystools.saas.exception.*;
import org.telosystools.saas.service.ProjectService;
import org.telosystools.saas.service.WorkspaceService;

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

    @Inject
    private WorkspaceService workspaceService;

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
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error_message", e.getMessage());
            return new ResponseEntity<>(responseHeaders,HttpStatus.NOT_FOUND);
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
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error_message", e.getMessage());
            return new ResponseEntity<>(responseHeaders,HttpStatus.UNAUTHORIZED);
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
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error_message", e.getMessage());
            return new ResponseEntity<>(responseHeaders,HttpStatus.CONFLICT);
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
     * Get the project's workspace
     *
     * @param projectId the project id
     * @return the workspace
     */
    @RequestMapping(value = "/{id}/workspace", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Workspace> getWorkspace(@PathVariable("id") String projectId) {
        try {
            return new ResponseEntity<>(workspaceService.getWorkspace(projectId),HttpStatus.OK);
        } catch (ProjectNotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error_message", e.getMessage());
            return new ResponseEntity<>(responseHeaders,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new file at the specified path.
     *
     * @param projectId The project id
     * @param fileData The file to add
     * @return a new File if created, error 404 if the parent folder doesn't exist
     */
    @RequestMapping(value = "/{id}/workspace/files", method = RequestMethod.POST)
    public ResponseEntity<File> createFile(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        try {
            return new ResponseEntity<>(workspaceService.createFile(fileData.getPath(), fileData.getContent(), projectId),
                    HttpStatus.CREATED);
        } catch (FolderNotFoundException | GridFSFileNotFoundException | ProjectNotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error_message", e.getMessage());
            return new ResponseEntity<>(responseHeaders,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Return the content of the given file.
     *
     * @param projectId Project ID
     * @param fileId GridFS File ID
     * @return The file content as a String
     */
    @RequestMapping(value = "/{id}/workspace/files/{fileId}", method = RequestMethod.GET)
    public @ResponseBody String getFileContent(@PathVariable("id") String projectId, @PathVariable String fileId) {
        return workspaceService.getFileContent(projectId, fileId);
    }


    /**
     * Update the content of the given file.
     * @param projectId Project ID
     * @param fileData The file path and content as a Json object
     */
    @RequestMapping(value = "/{projectId}/workspace/files/", method = RequestMethod.PUT)
    public ResponseEntity<File> updateFileContent(@PathVariable("projectId") String projectId, @RequestBody FileData fileData) {
        try {
            if (fileData.getContent() == null || fileData.getPath() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(workspaceService.updateFile(projectId, fileData.getPath(), fileData.getContent()), HttpStatus.OK);
        } catch (GridFSFileNotFoundException | ProjectNotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error_message", e.getMessage());
            return new ResponseEntity<>(responseHeaders, HttpStatus.NOT_FOUND);
        }
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
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error_message", e.getMessage());
            return new ResponseEntity<>(responseHeaders,HttpStatus.NOT_FOUND);
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
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error_message", e.getMessage());
            return new ResponseEntity<>(responseHeaders,HttpStatus.NOT_FOUND);
        }
    }
}

