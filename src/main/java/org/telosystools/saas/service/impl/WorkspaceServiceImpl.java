package org.telosystools.saas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telosystools.saas.bean.Path;
import org.telosystools.saas.dao.FileDao;
import org.telosystools.saas.dao.RootFolderDao;
import org.telosystools.saas.dao.WorkspaceDao;
import org.telosystools.saas.domain.filesystem.File;
import org.telosystools.saas.domain.filesystem.*;
import org.telosystools.saas.exception.FileNotFoundException;
import org.telosystools.saas.exception.FolderNotFoundException;
import org.telosystools.saas.exception.InvalidPathException;
import org.telosystools.saas.exception.ProjectNotFoundException;
import org.telosystools.saas.service.WorkspaceService;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by luchabou on 27/02/2015.
 * <p/>
 * Service de gestion du workspace contenant
 * des folders et des fichiers
 */
@Component
public class WorkspaceServiceImpl implements WorkspaceService {

    public static final String REGEX_FILENAME = "^([_A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+)$";
    public static final String REGEX_FOLDER = "[^_A-Za-z0-9/\\-]";
    public static final String REGEX_FOLDERS = REGEX_FOLDER + "*";
    @Autowired
    private WorkspaceDao workspaceDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private RootFolderDao rootFolderDao;

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
    public Workspace getWorkspace(String projectId) throws ProjectNotFoundException {
        Workspace workspace = workspaceDao.load(projectId);
        if (workspace != null) {
            return workspace;
        } else {
            throw new ProjectNotFoundException(projectId);
        }
    }

    @Override
    public Folder createFolder(String absolutePath, String projectId) throws FolderNotFoundException, ProjectNotFoundException, org.telosystools.saas.exception.InvalidPathException {
        if (absolutePath.matches(REGEX_FOLDERS)) throw new InvalidPathException(absolutePath);

        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(absolutePath);
        Folder folderParent = getFolderForPath(workspace, path.getParent());
        if (folderParent == null)
            throw new FolderNotFoundException(path.getBasename(), projectId);

        Folder folder = new Folder(path);
        folderParent.addFolder(folder);
        workspaceDao.save(workspace, projectId);
        return folder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renameFolder(String absolutePath, String folderName, String projectId) throws ProjectNotFoundException, InvalidPathException, FolderNotFoundException {
        if (folderName.matches(REGEX_FOLDER)) throw new InvalidPathException(folderName);
        if (absolutePath.matches(REGEX_FOLDERS)) throw new InvalidPathException(absolutePath);

        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(absolutePath);

        Folder folder = getFolderForPath(workspace, path);
        if (folder == null) throw new FolderNotFoundException(absolutePath, projectId);

        Folder folderParent = getFolderForPath(workspace, path.getParent());
        folderParent.removeFolder(folder);
        folder.changeName(folderName);
        folderParent.addFolder(folder);

        workspaceDao.save(workspace, projectId);
    }

    /**
     * Remove an existing folder.
     *
     * @param projectId    project unique identifier
     * @param absolutePath the path of the folder
     */
    @Override
    public void removeFolder(String absolutePath, String projectId) throws ProjectNotFoundException, FolderNotFoundException, InvalidPathException {
        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(absolutePath);

        if (path.getBasename().matches(REGEX_FOLDERS)) throw new InvalidPathException(absolutePath);

        Folder folder = getFolderForPath(workspace, path);

        if (folder == null)
            throw new FolderNotFoundException(path.getBasename(), projectId);

        for (File file : folder.getFilesAsList()) {
            fileDao.remove(file, projectId);
        }
        Folder folderParent = getFolderForPath(workspace, path.getParent());
        folderParent.getFolders().remove(folder.getName());
        workspaceDao.save(workspace, projectId);
    }

    /**
     * Create a new file in an existing folder.
     *
     * @param absolutePath Absolute path
     * @param content      File content as String
     * @param projectId    Project id
     */
    @Override
    public File createFile(String absolutePath, String content, String projectId) throws FolderNotFoundException, FileNotFoundException, ProjectNotFoundException, InvalidPathException {
        Path path = Path.valueOf(absolutePath);

        if (path.getBasename().matches(REGEX_FOLDERS)) throw new InvalidPathException(absolutePath);
        if (!path.getFilename().matches(REGEX_FILENAME)) throw new InvalidPathException(path.getFilename());

        Workspace workspace = getWorkspace(projectId);
        Folder folderParent = getFolderForPath(workspace, path.getParent());
        if (folderParent == null)
            throw new FolderNotFoundException(path.getBasename(), projectId);

        File file = new File(path);
        folderParent.addFile(file);
        fileDao.save(file, this.createInputStream(content == null ? "" : content), projectId);
        workspaceDao.save(workspace, projectId);

        return file;
    }

    /**
     * Rename an existing file
     *
     * @param absolutePath the path to the file to be renamed
     * @param fileName  the new name of the file
     * @param projectId project unique identifier
     */
    @Override
    public void renameFile(String absolutePath, String fileName, String projectId) throws ProjectNotFoundException, InvalidPathException, FileNotFoundException {
        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(absolutePath);

        if (path.getBasename().matches(REGEX_FOLDERS)) throw new InvalidPathException(absolutePath);
        if (!path.getFilename().matches(REGEX_FILENAME)) throw new InvalidPathException(path.getFilename());

        File file = getFileForPath(workspace, path);
        if (file == null) throw new FileNotFoundException(absolutePath);
        if (file.getName().equals(fileName)) return;

        Folder folderParent = getFolderForPath(workspace, path.getParent());
        folderParent.removeFile(file);
        file.changeName(fileName);
        folderParent.addFile(file);

        workspaceDao.save(workspace, projectId);
    }

    /**
     * Remove file from the folder.
     *
     * @param absolutePath Absolute path
     * @param projectId    Project id
     */
    @Override
    public void removeFile(String absolutePath, String projectId) throws ProjectNotFoundException, InvalidPathException, FileNotFoundException {
        Workspace workspace = getWorkspace(projectId);
        Path path = Path.valueOf(absolutePath);

        if (path.getBasename().matches(REGEX_FOLDERS)) throw new InvalidPathException(absolutePath);
        if (!path.getFilename().matches(REGEX_FILENAME)) throw new InvalidPathException(path.getFilename());

        File file = getFileForPath(workspace, path);
        if (file == null) throw new FileNotFoundException(absolutePath);
        Folder folderParent = getFolderForPath(workspace, path.getParent());

        fileDao.remove(file, projectId);
        folderParent.removeFile(file);

        workspaceDao.save(workspace, projectId);
    }

    /**
     * Indicates if the file exists ot not
     *
     * @param workspace workspace
     * @param path      path
     * @return boolean
     */
    public boolean exists(Workspace workspace, String path) {
        return getFolderForPath(workspace, Path.valueOf(path)) != null && getFileForPath(workspace, Path.valueOf(path)) != null;
    }

    /**
     * Get sub folder of the folder belong the path
     *
     * @param path Path
     * @return Sub folder
     */
    public Folder getFolderForPath(Workspace workspace, Path path) {
        Folder currentFolder = getRootFolderForPath(workspace, path);
        for (int i = 1; i < path.getNameCount(); i++) {
            String name = path.getName(i);
            if (currentFolder.getFolders().containsKey(name)) {
                currentFolder = currentFolder.getFolders().get(name);
            } else {
                return null;
            }
        }
        return currentFolder;
    }

    /**
     * Get sub folder of the folder belong the path
     *
     * @param path Path
     * @return Sub folder
     */
    public File getFileForPath(Workspace workspace, Path path) {
        Folder currentFolder = getRootFolderForPath(workspace, path);
        if (path.getNameCount() > 1) {
            for (int i = 1; i < path.getNameCount() - 1; i++) {
                String name = path.getName(i);
                if (currentFolder.getFolders().containsKey(name)) {
                    currentFolder = currentFolder.getFolders().get(name);
                } else {
                    return null;
                }
            }
        }
        if (path.getNameCount() > 0) {
            String name = path.getName(path.getNameCount() - 1).replace('.', Folder.DOT_REPLACEMENT);
            if (currentFolder.getFiles().containsKey(name)) {
                return currentFolder.getFiles().get(name);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public FileData getFileContent(String absolutePath, String projectId) throws ProjectNotFoundException, FileNotFoundException {
        final Workspace workspace = this.getWorkspace(projectId);
        final File file = this.getFileForPath(workspace, Path.valueOf(absolutePath));

        if (file == null) throw new FileNotFoundException("File not found in path");
        final String content = readInputStream(fileDao.loadContent(file.getGridFSId(), projectId));

        return new FileData(file.getAbsolutePath(), content, file.getName());
    }

    @Override
    public void updateFile(String absolutePath, String content, String projectId) throws ProjectNotFoundException, FileNotFoundException {
        final Workspace workspace = this.getWorkspace(projectId);
        final Path parsedPath = Path.valueOf(absolutePath);
        final RootFolder rootFolder = getRootFolderForPath(workspace, parsedPath);
        final File file = this.getFileForPath(workspace, parsedPath);

        if (file == null) throw new FileNotFoundException("File not found in path");

        // Sauvegarde dans GridFS. L'id GridFS est mis à jour dans le File
        fileDao.save(file, createInputStream(content), projectId);
        // Mise à jour du workspace
        rootFolderDao.save(rootFolder, projectId);
    }

    @Override
    public void deleteWorkspace(String projectId) {
        workspaceDao.delete(projectId);
    }

    /**
     * Return the root folder corresponding to the path
     *
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
