package org.telosystools.saas.domain.filesystem;

import org.telosystools.saas.bean.Path;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by luchabou on 27/02/2015.
 *
 * A folder. Contains folders and files.
 */
public class Folder implements Serializable {

    public static final char DOT_REPLACEMENT = '+';
    private String absolutePath;
    private String path;
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

    public void removeFolder(Folder folder) { this.folders.remove(folder.getName());}

    public void addFile(File file) {
        // TODO : Solution alternative pour gérer le '.', charactère interdit dans un field mongo
        this.files.put(file.getName().replace('.', DOT_REPLACEMENT), file);
    }

    public void removeFile(File file) {
        this.files.remove(file.getName().replace('.', DOT_REPLACEMENT));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Folder)) return false;

        Folder folder = (Folder) o;

        return absolutePath.equals(folder.absolutePath)
                && files.equals(folder.files)
                && folders.equals(folder.folders)
                && name.equals(folder.name)
                && path.equals(folder.path);

    }

    public void updatePath(Path newPath) {
        this.absolutePath = newPath.toString();
        this.path = newPath.getBasename();

        for (File file : getFiles().values())
            file.updatePath(newPath);

        for (Folder folder : getFolders().values()) {
            Path childrenPath = Path.valueOf(this.absolutePath, folder.getName());
            folder.updatePath(childrenPath);
        }
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
        this.updatePath(Path.valueOf(this.path + "/" + name));
    }
}
