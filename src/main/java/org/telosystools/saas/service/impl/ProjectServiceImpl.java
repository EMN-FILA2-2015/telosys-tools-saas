package org.telosystools.saas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.repository.ProjectRepository;
import org.telosystools.saas.service.ProjectService;

import java.util.List;

/**
 * Created by Adrian on 29/01/15.
 */
@Component
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository repository;


    @Override
    public List<Project> list() {
        return repository.findAll();
    }

    @Override
    public Project find(String id) {
        return repository.findOne(id);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public Project insert(Project project) {
        return repository.save(project);
    }

    public void setRepository(ProjectRepository repository) {
        this.repository = repository;
    }
}
