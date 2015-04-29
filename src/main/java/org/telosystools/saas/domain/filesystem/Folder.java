package org.telosystools.saas.domain.filesystem;

import org.telosystools.saas.bean.Path;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by luchabou on 27/02/2015.
 */
public class Folder implements Serializable {

    private String absolutePath;
    private final String path;
    private String name;
    private final Map<String, Folder> folders = new TreeMap<>();
    private final Map<String, File> files = new TreeMap<>();

    Folder() {
        this.absolutePath = null;
        this.path = null;
        this.name = null;
    }

    public Folder(String absolutePath) {
        this.absolutePath = absolutePath;
        Path path = Path.valueOf(absolutePath);
        this.path = path.getBasename();
        this.name = path.getFilename();
    }

    public Folder(Path path) {
        this.absolutePath = path.toString();
        this.path = path.getBasename();
        this.name = path.getFilename();
    }

    public void addFolder(Folder folder) {
        this.folders.put(folder.getName(), folder);
    }

    public void addFile(File file) {
        this.files.put(file.getName(), file);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public Map<String, Folder> getFolders() {
        return folders;
    }

    public Map<String, File> getFiles() {
        return files;
    }

    public List<Folder> getFoldersAsList() {
        return new ArrayList<>(folders.values());
    }

    public List<File> getFilesAsList() {
        return new ArrayList<>(files.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Folder)) return false;

        Folder folder = (Folder) o;

        if (!absolutePath.equals(folder.absolutePath)) return false;
        if (!files.equals(folder.files)) return false;
        if (!folders.equals(folder.folders)) return false;
        if (!name.equals(folder.name)) return false;
        if (!path.equals(folder.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = absolutePath.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + folders.hashCode();
        result = 31 * result + files.hashCode();
        return result;
    }

    public void changeName(String folderName) {
        this.name = folderName;
        this.absolutePath = this.path+"/"+name;
    }
}
