package org.telosystools.saas.dao;

import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
import org.telosystools.saas.domain.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

/**
 * Created by luchabou on 27/02/2015.
 */
@Repository
public class FileDao {

    @Autowired
    private GridFSDao gridFSDao;

    public InputStream loadContent(String fileId, String database) {
        if(fileId == null) {
            return null;
        }
        InputStream in = gridFSDao.load(fileId, database);
        return in;
    }

    public void save(File file, InputStream in, String database) {
        if(file.getGridFSId() == null) {
            String gridFSId = gridFSDao.create(in, database);
            file.setGridFSId(gridFSId);
        } else {
            gridFSDao.update(file.getGridFSId(), in, database);
        }
    }
    
    public void saveContent(String fileId, InputStream in, String database) {
        gridFSDao.update(fileId, in, database);
    }
    
    public void remove(File file, String database) {
        if(file.getGridFSId() != null) {
            gridFSDao.remove(file.getGridFSId(), database);
        }
    }
}
