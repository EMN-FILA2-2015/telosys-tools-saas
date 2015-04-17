package org.telosystools.saas.dao;

import com.mongodb.Mongo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.telosystools.saas.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * DAO : projects
 */
@Repository
public class ProjectDao {

    private static final String COLLECTION_PROJECTS = "projects";

    @Autowired
    private Mongo mongo;

    @Autowired
    private MongoTemplate mongoTemplateGeneral;

    public Project load(String projectId) {
        return mongoTemplateGeneral.findById(projectId, Project.class, COLLECTION_PROJECTS);
    }

    public void save(Project project) {
        mongoTemplateGeneral.save(project, COLLECTION_PROJECTS);
    }

    public void remove(String projectId) {
        mongoTemplateGeneral.remove(projectId);
    }

    public List<Project> findAll() {
        return mongoTemplateGeneral.findAll(Project.class, COLLECTION_PROJECTS);
    }

    public List<Project> findByUser(String userLogin) {
        return mongoTemplateGeneral.find(new Query(where("ownerId").is(userLogin)), Project.class);
    }

}
