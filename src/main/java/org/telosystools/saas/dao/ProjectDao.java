package org.telosystools.saas.dao;

import com.mongodb.Mongo;
import org.telosystools.saas.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * DAO : projects
 */
@Repository
public class ProjectDao {

    private static final String COLLECTION_FOLDERS = "docs";

    @Autowired
    private Mongo mongo;

    MongoTemplate mongoTemplate(String database) {
        return new MongoTemplate(mongo, database);
    }

    public Project load(String database) {
        return mongoTemplate(database)
                .findById(Project.ID, Project.class, COLLECTION_FOLDERS);
    }

    public void save(Project folder, String database) {
        mongoTemplate(database)
                .save(folder, COLLECTION_FOLDERS);
    }

    public void remove(String database) {
        mongo.dropDatabase(database);
    }

}
