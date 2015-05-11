package org.telosystools.saas.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.telosystools.saas.domain.filesystem.RootFolder;
import org.telosystools.saas.domain.filesystem.Workspace;

/**
 * Created by luchabou on 27/02/2015.
 *
 * The workspace dao.
 */
@Repository
public class WorkspaceDao {

    @Autowired
    private RootFolderDao rootFolderDao;

    public Workspace load(String database) {
        if (rootFolderDao.workspaceExists(database)) {
            Workspace workspace = new Workspace();
            refresh(workspace, database);
            return workspace;
        }
        return null;
    }

    public void refresh(Workspace workspace, String database) {

        RootFolder modelsFolder = rootFolderDao.findById(RootFolder.ID_PREFIX+Workspace.MODELS, database);
        RootFolder templatesFolder = rootFolderDao.findById(RootFolder.ID_PREFIX+Workspace.TEMPLATES, database);
        RootFolder generatedsFolder = rootFolderDao.findById(RootFolder.ID_PREFIX+Workspace.GENERATED, database);

        workspace.setModels(modelsFolder);
        workspace.setTemplates(templatesFolder);
        workspace.setGenerated(generatedsFolder);
    }

    public void save(Workspace workspace, String database) {
        rootFolderDao.save(workspace.getModels(), database);
        rootFolderDao.save(workspace.getTemplates(), database);
        rootFolderDao.save(workspace.getGenerated(), database);
    }

    public void delete(String database) {
        rootFolderDao.mongoTemplateDatabase(database).getDb().dropDatabase();
    }

}
