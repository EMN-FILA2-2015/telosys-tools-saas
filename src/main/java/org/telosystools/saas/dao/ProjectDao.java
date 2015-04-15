package org.telosystools.saas.dao;

import com.mongodb.Mongo;
import org.telosystools.saas.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO : projects
 */
@Repository
public class ProjectDao {

    private static final String COLLECTION_FOLDERS = "docs";

    @Autowired
    private Mongo mongo;

    MongoTemplate mongoTemplate(String database) {
        // Warn : Database name must only contain letters, numbers, underscores and dashes!
        return new MongoTemplate(mongo, database);
    }

    public Project load(String database) {
        // WARN : si la base n'existe pas, en crée une vide
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

    public List<String> findAll() {
        return mongo.getDatabaseNames();
    }

}
