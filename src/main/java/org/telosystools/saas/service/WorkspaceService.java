package org.telosystools.saas.service;

import org.telosystools.saas.domain.filesystem.FileData;
import org.telosystools.saas.domain.filesystem.RootFolder;
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
     * Creates a standard workspace
     *
     * @param projectId Project id
     * @return Workspace
     */
    Workspace createWorkspace(String projectId);

    /**
     * Saves the workspace
     *
     * @param workspace Workspace
     */
    void saveWorkspace(Workspace workspace, String projectId);

    /**
     * Deletes the workspace
     *
     * @param projectId Id of the workspace's project
     */
    void deleteWorkspace(String projectId);

    /**
     * Returns the workspace for the project
     *
     * @param projectId Project id
     * @return Workspace
     */
    Workspace getWorkspace(String projectId) throws ProjectNotFoundException;

    /**
     * Creates a new folder in the folder.
     * @param absolutePath Absolute path
     * @param projectId Project id
     * @return updated folder tree
     */
    RootFolder createFolder(String absolutePath, String projectId) throws FolderNotFoundException, ProjectNotFoundException, InvalidPathException;

    /**
     * Removes an existing folder.
     *
     * @param absolutePath the path of the folder
     * @param projectId    project unique identifier
     */
    void removeFolder(String absolutePath, String projectId) throws ProjectNotFoundException, FolderNotFoundException, InvalidPathException;

    /**
     * Renames a folder.
     *
     * @param absolutePath the path to the folder
     * @param folderName the new folder name
     * @param projectId Project ID
     * @throws ProjectNotFoundException
     * @throws FolderNotFoundException
     * @throws InvalidPathException
     */
    void renameFolder(String absolutePath, String folderName, String projectId) throws ProjectNotFoundException, InvalidPathException, FolderNotFoundException;

    /**
     * Creates a new file in an existing folder.
     *
     * @param absolutePath Absolute path
     * @param content File content as String
     * @param projectId Project id
     * @return Updated folder tree
     */
    RootFolder createFile(String absolutePath, String content, String projectId) throws FolderNotFoundException, FileNotFoundException, ProjectNotFoundException, InvalidPathException;

    /**
     * Deletes a File.
     *
     * @param absolutePath Absolute path
     * @param projectId Project ID
     * @throws ProjectNotFoundException
     * @throws FileNotFoundException
     * @throws InvalidPathException
     */
    void removeFile(String absolutePath, String projectId) throws ProjectNotFoundException, FileNotFoundException, InvalidPathException;

    /**
     * Renames a file.
     *
     * @param absolutePath the path to the file
     * @param fileName the new filename
     * @param projectId Project ID
     * @throws ProjectNotFoundException
     * @throws FileNotFoundException
     * @throws InvalidPathException
     */
    void renameFile(String absolutePath, String fileName, String projectId) throws ProjectNotFoundException, FileNotFoundException, InvalidPathException;

    /**
     * Returns the file's content.
     *
     * @param absolutePath path du fichier
     * @param projectId Id du projet
     * @return contenu du fichier
     */
    FileData getFileContent(String absolutePath, String projectId) throws ProjectNotFoundException, FileNotFoundException;

    /**
     * Updates the file's content.
     *
     * @param absolutePath path du fichier
     * @param projectId Id du projet
     * @param content Contenu a sauvegarder
     */
    void updateFile(String absolutePath, String content, String projectId) throws ProjectNotFoundException, FileNotFoundException;

}
