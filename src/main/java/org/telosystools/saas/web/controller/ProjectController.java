package org.telosystools.saas.web.controller;

import org.springframework.http.HttpStatus;
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
    public Project get(@PathVariable("id") String id) {
        return projectService.loadProject(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Project> getAll() { return projectService.list(); }

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
            return null;
        }
    }



}