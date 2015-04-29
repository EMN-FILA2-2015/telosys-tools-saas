package org.telosystools.saas.service;

import org.telosystools.saas.domain.filesystem.File;
import org.telosystools.saas.domain.filesystem.Folder;
import org.telosystools.saas.domain.filesystem.Workspace;
import org.telosystools.saas.exception.FolderNotFoundException;
import org.telosystools.saas.exception.GridFSFileNotFoundException;
import org.telosystools.saas.exception.ProjectNotFoundException;

/**
 * Created by Adrian on 20/04/15.
 *
 * Workspace management service interface.
 */
public interface WorkspaceService {

    /**
     * Create a standard workspace
     * @param projectId Project id
     * @return Workspace
     */
    Workspace createWorkspace(String projectId);

    /**
     * Save the workspace
     *
     * @param workspace Workspace
     */
    void saveWorkspace(Workspace workspace, String projectId);

    /**
     * Delete the workspace
     *
     * @param projectId Id of the workspace's project
     */
    void deleteWorkspace(String projectId);

    /**
     * Return the workspace for the project
     * @param projectId Project id
     * @return Workspace
     */
    Workspace getWorkspace(String projectId) throws ProjectNotFoundException;

    /**
     * Create a new folder in the folder.
     * @param absolutePath Absolute path
     * @param projectId Project id
     */
    Folder createFolder(String absolutePath, String projectId) throws FolderNotFoundException, ProjectNotFoundException;

    /**
     * Create a new file in an existing folder.
     * @param absolutePath Absolute path
     * @param content File content as String
     * @param projectId Project id
     */
    File createFile(String absolutePath, String content, String projectId) throws FolderNotFoundException, GridFSFileNotFoundException, ProjectNotFoundException;

    /**
     * Retourne le contenu du fichier stocké en base
     *
     * @param projectId Id du projet
     * @param fileId GridFS id du fichier
     * @return contenu du fichier
     */
    String getFileContent(String projectId, String fileId);

    /**
     * Ecrase le contenu du fichier
     *
     * @param projectId Id du projet
     * @param fileId GridFS id du fichier
     * @param content Contenu a sauvegarder
     */
    String updateFileContent(String projectId, String fileId, String content) throws GridFSFileNotFoundException;

}
