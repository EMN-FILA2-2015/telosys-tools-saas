package org.telosystools.saas.service;

import org.telosystools.saas.domain.filesystem.File;
import org.telosystools.saas.domain.filesystem.FileData;
import org.telosystools.saas.domain.filesystem.Folder;
import org.telosystools.saas.domain.filesystem.Workspace;
import org.telosystools.saas.exception.FileNotFoundException;
import org.telosystools.saas.exception.FolderNotFoundException;
import org.telosystools.saas.exception.InvalidPathException;
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
     * @return Folder créé
     */
    Folder createFolder(String absolutePath, String projectId) throws FolderNotFoundException, ProjectNotFoundException, InvalidPathException;

    /**
     * Remove an existing folder.
     *
     * @param absolutePath the path of the folder
     * @param projectId    project unique identifier
     */
    void removeFolder(String absolutePath, String projectId) throws ProjectNotFoundException, FolderNotFoundException, InvalidPathException;

    /**
     * Create a new file in an existing folder.
     * @param absolutePath Absolute path
     * @param content File content as String
     * @param projectId Project id
     * @return File créé
     */
    File createFile(String absolutePath, String content, String projectId) throws FolderNotFoundException, FileNotFoundException, ProjectNotFoundException, InvalidPathException;

    /**
     * Delete a File.
     *
     * @param absolutePath Absolute path
     * @param projectId Project ID
     * @throws ProjectNotFoundException
     * @throws FileNotFoundException
     * @throws InvalidPathException
     */
    void removeFile(String absolutePath, String projectId) throws ProjectNotFoundException, FileNotFoundException, InvalidPathException;

    /**
     * Retourne le contenu du fichier stocké en base
     *
     * @param path path du fichier
     * @param projectId Id du projet
     * @return contenu du fichier
     */
    FileData getFileContent(String path, String projectId) throws ProjectNotFoundException, FileNotFoundException;

    /**
     * Ecrase le contenu du fichier
     *
     * @param path path du fichier
     * @param projectId Id du projet
     * @param content Contenu a sauvegarder
     */
    void updateFile(String path, String content, String projectId) throws ProjectNotFoundException, FileNotFoundException;

}
