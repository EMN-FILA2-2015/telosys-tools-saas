package org.telosystools.saas.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.telosystools.saas.domain.filesystem.File;
import org.telosystools.saas.exception.FileNotFoundException;

import java.io.InputStream;

/**
 * Created by luchabou on 27/02/2015.
 *
 * Service delegate to GridFSDao
 */
@Repository
public class FileDao {

    @Autowired
    private GridFSDao gridFSDao;

    public InputStream loadContent(String fileId, String database) throws FileNotFoundException {
        if(fileId == null) {
            return null;
        }
        return gridFSDao.load(fileId, database);
    }

    public void save(File file, InputStream in, String database) throws FileNotFoundException {
        if(file.getGridFSId() == null) {
            String gridFSId = gridFSDao.create(in, database);
            file.setGridFSId(gridFSId);
        } else {
            file.setGridFSId(gridFSDao.update(file.getGridFSId(), in, database));
        }
    }

    public void remove(File file, String database) {
        if(file.getGridFSId() != null) {
            gridFSDao.remove(file.getGridFSId(), database);
        }
    }
}
