package org.telosystools.saas.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.telosystools.saas.domain.Project;

import java.util.List;

/**
 * Created by Adrian on 20/04/15.
 */
public interface ProjectRepository extends MongoRepository<Project, String> {

    List<Project> findByOwner(String owner);

}
