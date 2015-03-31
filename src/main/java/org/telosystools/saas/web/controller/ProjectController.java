package org.telosystools.saas.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.service.ProjectService;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Project get(@PathVariable("id") String id) {
        return projectService.find(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Project> getAll() { return projectService.list(); }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Project create(@RequestBody Project project) {
        return projectService.insert(project);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") String id) { projectService.delete(id); }
}