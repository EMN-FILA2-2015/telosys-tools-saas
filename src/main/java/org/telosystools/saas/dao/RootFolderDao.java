package org.telosystools.saas.dao;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.telosystools.saas.domain.filesystem.RootFolder;

/**
 * Created by luchabou on 27/02/2015.
 */
@Repository
public class RootFolderDao {

    private static final String COLLECTION_FOLDERS = "folders";

    @Autowired
    private Mongo mongo;

    MongoTemplate mongoTemplateDatabase(String database) {
        return new MongoTemplate(mongo, database);
    }

    public boolean workspaceExists(String database) {
        return mongo.getDatabaseNames().contains(database);
    }

    public RootFolder findById(String folderId, String database) {
        return mongoTemplateDatabase(database)
                .findById(folderId, RootFolder.class, COLLECTION_FOLDERS);
    }

    public void save(RootFolder folder, String database) {
        mongoTemplateDatabase(database)
                .save(folder, COLLECTION_FOLDERS);
    }

    public void clean(String folderId, String database) {
        RootFolder folder = findById(folderId, database);
        mongoTemplateDatabase(database)
                .remove(folder, COLLECTION_FOLDERS);
        // TODO : suppression des fichiers dans gridfs
    }

}
