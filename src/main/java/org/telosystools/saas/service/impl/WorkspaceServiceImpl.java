package org.telosystools.saas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telosystools.saas.bean.Path;
import org.telosystools.saas.dao.FileDao;
import org.telosystools.saas.dao.WorkspaceDao;
import org.telosystools.saas.domain.File;
import org.telosystools.saas.domain.*;
import org.telosystools.saas.exception.FolderNotFoundException;
import org.telosystools.saas.exception.GridFSFileNotFoundException;
import org.telosystools.saas.service.WorkspaceService;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by luchabou on 27/02/2015.
 *
 * Service de gestion du workspace contenant
 * des folders et des fichiers
 */
@Component
public class WorkspaceServiceImpl implements WorkspaceService{

    @Autowired
    private WorkspaceDao workspaceDao;
    @Autowired
    private FileDao fileDao;

    @Override
    public Workspace createWorkspace(String projectId) {
        Workspace workspace = new Workspace();

        workspace.setModels(new RootFolder(Workspace.MODELS));
        workspace.setTemplates(new RootFolder(Workspace.TEMPLATES));
        workspace.setGenerateds(new RootFolder(Workspace.GENERATEDS));
        workspace.setSettings(new RootFolder(Workspace.SETTINGS));

        workspaceDao.save(workspace, projectId);

        return workspace;
    }

    @Override
    public void saveWorkspace(Workspace workspace, String projectId) {
        workspaceDao.save(workspace, projectId);
    }

    @Override
    public Workspace getWorkspace(String projectId) {
        //TODO: handle null case
        return workspaceDao.load(projectId);
    }

    @Override
    public Folder createFolder(String absolutePath, String projectId) throws FolderNotFoundException {
        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(absolutePath);
        Folder folder = new Folder(path);
        Folder folderParent = getFolderForPath(workspace, path.getParent());
        if (folderParent!=null) {
            folderParent.addFolder(folder);
            workspaceDao.save(workspace, projectId);
            return folder;
        } else {
            throw new FolderNotFoundException(path.getBasename(),projectId);
        }
    }

    /**
     * Rename an existing folder.
     * @param folder the folder to be renamed.
     * @param folderName the new name of the folder
     * @param projectId project unique identifier
     */
    public void renameFolder(Folder folder, String folderName, String projectId) {
        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(folder.getPath(), folder.getName());
        Folder folderParent = getFolderForPath(workspace, path.getParent());
        if (folderParent!=null) {
            folderParent.getFolders().remove(folder.getName());
            folder.changeName(folderName);
            folderParent.getFolders().put(folderName,folder);
            workspaceDao.save(workspace, projectId);
        }
    }

    /**
     * Remove an existing folder.
     * @param absolutePath the path of the folder
     * @param projectId project unique identifier
     */
    public void removeFolder(String absolutePath, String projectId) {
        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(absolutePath);
        Folder folder = getFolderForPath(workspace, path);
        for (File file : folder.getFilesAsList()) {
            fileDao.remove(file, projectId);
        }
        Folder folderParent = getFolderForPath(workspace, path.getParent());
        folderParent.getFolders().remove(folder.getName());
        workspaceDao.save(workspace, projectId);
    }

    /**
     * Create a new file in an existing folder.
     * @param absolutePath Absolute path
     * @param content File content as String
     * @param projectId Project id
     */
    @Override
    public File createFile(String absolutePath, String content, String projectId) throws FolderNotFoundException, GridFSFileNotFoundException {
        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(absolutePath);
        File file = new File(path);
        Folder folderParent = getFolderForPath(workspace, path.getParent());
        if (folderParent != null) {
            folderParent.addFile(file);
            fileDao.save(file, this.createInputStream(content), projectId);
            workspaceDao.save(workspace, projectId);
            return file;
        } else {
            throw new FolderNotFoundException(path.getBasename(),projectId);
        }
    }

    /**
     * Update an existing file.
     *
     * @param file File
     * @param in File content
     * @param projectId Project id
     */
    public void saveFile(File file, InputStream in, String projectId) throws GridFSFileNotFoundException {
        fileDao.save(file, in, projectId);
    }

    /**
     * Rename an existing file
     * @param file the file to be renamed
     * @param fileName the new name of the file
     * @param projectId project unique identifier
     */
    public void renameFile(File file, String fileName, String projectId) {
        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(file.getPath(), file.getName());
        Folder folderParent = getFolderForPath(workspace, path.getParent());
        if (folderParent!=null) {
            folderParent.getFiles().remove(file.getName());
            file.changeName(fileName);
            folderParent.getFiles().put(fileName,file);
            workspaceDao.save(workspace, projectId);
        }
    }

    /**
     * Remove file from the folder.
     * @param absolutePath Absolute path
     * @param projectId Project id
     */
    public void removeFile(String absolutePath, String projectId) {
        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(absolutePath);
        File file = getFileForPath(workspace, path);
        Folder folderParent = getFolderForPath(workspace, path.getParent());
        if (file!=null) fileDao.remove(file, projectId);
        if (file!=null && folderParent!=null) folderParent.getFiles().remove(file.getName());
        workspaceDao.save(workspace, projectId);
    }

    /**
     * Return the file extension in the file name.
     * @param filename file name
     * @return file extension
     */
    public String getFileExtension(String filename) {
        if(filename == null) {
            return null;
        }
        int posDot = filename.lastIndexOf("*");
        if(posDot != -1) {
            return filename.substring(posDot+1);
        }
        return File.FILE_EXTENSION_UNKNOWN;
    }

    /**
     * Indicates if the file exists ot not
     *
     * @param workspace workspace
     * @param path path
     * @return boolean
     */
    public boolean exists(Workspace workspace, String path) {
        return getFolderForPath(workspace, Path.valueOf(path)) != null && getFileForPath(workspace, Path.valueOf(path)) != null;
    }

    /**
     * Get sub folder of the folder belong the path
     * @param path Path
     * @return Sub folder
     */
    public Folder getFolderForPath(Workspace workspace, Path path) {
        Folder currentFolder = getRootFolderForPath(workspace, path);
        for(int i=1; i<path.getNameCount(); i++) {
            String name = path.getName(i);
            if(currentFolder.getFolders().containsKey(name)) {
                currentFolder = currentFolder.getFolders().get(name);
            } else {
                return null;
            }
        }
        return currentFolder;
    }

    /**
     * Get sub folder of the folder belong the path
     * @param path Path
     * @return Sub folder
     */
    public File getFileForPath(Workspace workspace, Path path) {
        Folder currentFolder = getRootFolderForPath(workspace, path);
        if(path.getNameCount() > 1) {
            for (int i = 1; i < path.getNameCount() - 1; i++) {
                String name = path.getName(i);
                if (currentFolder.getFolders().containsKey(name)) {
                    currentFolder = currentFolder.getFolders().get(name);
                } else {
                    return null;
                }
            }
        }
        if(path.getNameCount() > 0) {
            String name = path.getName(path.getNameCount() - 1);
            if(currentFolder.getFiles().containsKey(name)) {
                return currentFolder.getFiles().get(name);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public String getFileContent(String projectId, String fileId) {
        return readInputStream(fileDao.loadContent(fileId, projectId));
    }

    @Override
    public String updateFileContent(String projectId, String fileId, String content) throws GridFSFileNotFoundException {
        return fileDao.updateContent(fileId, createInputStream(content), projectId);
    }

    @Override
    public void deleteWorkspace(String projectId) {
        workspaceDao.delete(projectId);
    }

    /**
     * Return the root folder corresponding to the path
     * @param path Path
     * @return Root folder
     */
    public RootFolder getRootFolderForPath(Workspace workspace, Path path) {
        return workspace.getRootFolderByName(path.getRootName());
    }

    /**
     * Create an Inputstream from a String
     *
     * @param string String content
     * @return content as a stream
     */
    private InputStream createInputStream(String string) {
        return new ByteArrayInputStream(string.getBytes());
    }

    /**
     * Read a String from an Inputstream
     *
     * @param in the stream
     * @return content as String
     */
    private String readInputStream(InputStream in) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(in, UTF_8));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
