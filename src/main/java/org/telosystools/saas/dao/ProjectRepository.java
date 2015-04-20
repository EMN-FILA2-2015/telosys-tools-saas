package org.telosystools.saas.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.telosystools.saas.domain.Project;

import java.util.List;

/**
 * Created by Adrian on 20/04/15.
 */
@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

    List<Project> findByOwner(String owner);

}
