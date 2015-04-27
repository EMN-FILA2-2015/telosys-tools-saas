package org.telosystools.saas.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telosystools.saas.domain.*;
import org.telosystools.saas.service.ProjectService;
import org.telosystools.saas.service.WorkspaceService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Adrian on 29/01/15.
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
        // TODO : Try - Catch ProjectNotFoundException
        Project result = projectService.loadProject(id);
        return new ResponseEntity<>(result, null, result == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    /**
     * Get a list of all projects.
     *
     * @return the project list
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Project> getAllProjects() { return projectService.findAllByUser(); }

    /**
     * Create a project
     * @param project The Project to create
     * @return The project created
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Project createProject(@RequestBody Project project) {
        // TODO : Exception handling
        return projectService.createProject(project);
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
     * Get the project's worskapce
     *
     * @param projectId the project id
     * @return the workspace
     */
    @RequestMapping(value = "/{id}/workspace", method = RequestMethod.GET)
    public @ResponseBody Workspace getWorkspace(@PathVariable("id") String projectId) {
        return workspaceService.getWorkspace(projectId);
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
        } catch (FolderNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
     *
     * @param projectId Project ID
     * @param fileId GridFS File ID
     * @param fileContent The file content as a String
     */
    @RequestMapping(value = "/{projectId}/workspace/files/{fileId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateFileContent(@PathVariable("projectId") String projectId, @PathVariable("fileId") String fileId, @RequestBody String fileContent) {
        workspaceService.updateFileContent(projectId, fileId, fileContent);
    }

    /**
     * Update the project's configuration.
     *
     * @param projectId Project ID
     * @param projectConfig Project configuration
     */
    @RequestMapping(value = "/{id}/config/telosystoolscfg", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void setProjectConfig(@PathVariable("id") String projectId, @RequestBody ProjectConfiguration projectConfig) {
        projectService.updateProjectConfig(projectId, projectConfig);
    }

    /**
     * Get the project's configuration.
     *
     * @param projectId Project ID
     * @return the Project configuration
     */
    @RequestMapping(value = "/{id}/config/telosystoolscfg", method = RequestMethod.GET)
    public  @ResponseBody ProjectConfiguration getProjectConfiguration(@PathVariable("id") String projectId) {
        return projectService.loadProject(projectId).getProjectConfiguration();
    }
}

