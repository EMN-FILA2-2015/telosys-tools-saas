package org.telosystools.saas.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.telosystools.saas.domain.Project;

/**
 * Created by Adrian on 29/01/15.
 */
public interface ProjectRepository extends MongoRepository<Project, String> {
}
