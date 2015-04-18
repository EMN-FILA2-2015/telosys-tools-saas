package org.telosystools.saas.web.controller;

import org.springframework.core.io.InputStreamResource;
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Project> get(@PathVariable("id") String id) {
        Project result = projectService.loadProject(id);
        return new ResponseEntity<>(result, null, result == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Project> getAll() { return projectService.listByUser(); }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Project create(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") String id) { projectService.delete(id); }

    @RequestMapping(value = "/{id}/workspace", method = RequestMethod.GET)
    public @ResponseBody Workspace getWorkspace(@PathVariable("id") String projectId) {
        return workspaceService.getWorkspace(projectId);
    }

    @RequestMapping(value = "/{id}/workspace/files", method = RequestMethod.POST)
    public @ResponseBody File createFile(@PathVariable("id") String projectId, @RequestBody String fileName) {
        try {
            return workspaceService.createFile(fileName, null, projectId);
        } catch (FolderNotFoundException e) {
            return null; // ResponseEntity() ?
        }
    }

    @RequestMapping(value = "/{id}/workspace/files/{fileId}", method = RequestMethod.GET)
    public @ResponseBody String getFileContent(@PathVariable("id") String projectId, @PathVariable String fileId) {
        return workspaceService.getFileContent(fileId, projectId);
    }

    @RequestMapping(value = "/{projectId}/workspace/files/{fileId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void getFileContent(@PathVariable("projectId") String projectId, @PathVariable("fileId") String fileId, @RequestBody String fileContent) {
        workspaceService.setFileContent(projectId, fileId, fileContent);
    }

    @RequestMapping(value = "/projects/{id}/config/telosystoolscfg", method = RequestMethod.POST)
    public void setProjectConfig(@PathVariable("id") String projectId, @RequestBody ProjectConfiguration projectConfig) {
        projectService.updateProjectConfig(projectId, projectConfig);
    }

    @RequestMapping(value = "/projects/{id}/config/telosystoolscfg", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public  @ResponseBody ProjectConfiguration getProjectConfiguration(@PathVariable("id") String projectId) {
        return projectService.loadProject(projectId).getProjectConfiguration();
    }
}

