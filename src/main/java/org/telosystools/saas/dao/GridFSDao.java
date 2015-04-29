package org.telosystools.saas.dao;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.telosystools.saas.exception.GridFSFileNotFoundException;

import java.io.InputStream;

/**
 * Created by luchabou on 27/02/2015.
 *
 * File management with GridFS. One database is set per workspace.
 * See POC project Mongodb2 for details
 */
@Repository
class GridFSDao {

    @Autowired
    private Mongo mongo;

    GridFS gridFS(String database) {
        DB db = mongo.getDB(database);
        return new GridFS(db);
    }

    public InputStream load(String gridFSId, String database) {
        GridFSDBFile gridFSDBFile =
                gridFS(database).findOne(new ObjectId(gridFSId));
        if(gridFSDBFile == null) {
            throw new MongoException("File not found in GridFS : "+gridFSId);
        }
        return gridFSDBFile.getInputStream();
    }

    public String create(InputStream in, String database) {
        GridFSInputFile gridFSInputFile = gridFS(database).createFile(in);
        gridFSInputFile.save();

        return gridFSInputFile.getId().toString();
    }

    public String update(String gridFSId, InputStream in, String database) throws GridFSFileNotFoundException {
        // Récupération de l'ancien fichier et suppression
        final GridFSDBFile oldFile = gridFS(database).findOne(new ObjectId(gridFSId));
        if (oldFile != null) {
            GridFSInputFile updatedFile = gridFS(database).createFile(in, oldFile.getFilename());
            updatedFile.save();
            gridFS(database).remove(oldFile);
            return updatedFile.getId().toString();
        } else {
            throw new GridFSFileNotFoundException(gridFSId);
        }
    }

    public void remove(String gridFSId, String database) {
        gridFS(database).remove(new ObjectId(gridFSId));
    }

}
