package org.telosystools.saas.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Path
 */
public class Path implements Serializable {

    public static final String SEPARATOR = "/";

    /**
     * Elements of the path : folders names and file name.
     */
    private List<String> items = new ArrayList<>();

    /**
     * Get path items : folders names and file name
     * @return path items
     */
    public List<String> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Constructor : empty path
     */
    public Path() {
    }

    /**
     * Constructore : path with some path items
     * @param items Path items
     */
    Path(List<String> items) {
        this.items = new ArrayList<>(items);
    }

    /**
     * Return the full path as a string value
     * @return full path
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        boolean isFirst = true;
        for(String item : items) {
            if(isFirst) {
                isFirst = false;
            } else {
                str.append(SEPARATOR);
            }
            str.append(item);
        }
        return str.toString();
    }

    /**
     * Return the path from one or many paths
     * @param paths Paths
     * @return path
     */
    public static Path valueOf(String... paths) {
        Path pathResult = new Path();
        if(paths != null) {
            for(String path : paths) {
                String[] items = path.split(SEPARATOR);
                for (String item : items) {
                    pathResult.items.add(item);
                }
            }
        }
        return pathResult;
    }

    /**
     * Return the length of the path
     * @return length
     */
    public int getNameCount() {
        return items.size();
    }

    /**
     * Return file name or folder name
     * @return File name or folder name
     */
    public String getFilename() {
        if(items.size() == 0) {
            return null;
        }
        return items.get(items.size() - 1);
    }

    /**
     * Return name of a path item
     * @param index Index of the path item
     * @return Path item name
     */
    public String getName(int index) {
        if(index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    /**
     * Return parent folder path.
     * @return Parent folder path
     */
    public String getBasename() {
       return getParent().toString();
    }

    /**
     * Return parent path
     * @return path
     */
    public Path getParent() {
        List<String> baseItems = new ArrayList<>();
        for(int i=0; i<this.items.size()-1; i++) {
            baseItems.add(this.items.get(i));
        }
        Path basePath = new Path(baseItems);
        return basePath;
    }

    /**
     * Join two paths
     * @param path1 Path1
     * @param path2 Path2
     * @return paths
     */
    public static String join(String path1, String path2) {
        return path1 + SEPARATOR + path2;
    }

    /**
     * Return the root folder corresponding to the path
     * @return Root folder name
     */
    public Path getRoot() {
        if(this.items.isEmpty()) {
            return null;
        }
        return Path.valueOf(items.get(0));
    }

    /**
     * Return the root folder corresponding to the path
     * @return Root folder name
     */
    public String getRootName() {
        if(this.items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

}
