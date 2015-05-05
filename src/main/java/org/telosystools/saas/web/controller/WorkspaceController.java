package org.telosystools.saas.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.telosystools.saas.domain.filesystem.File;
import org.telosystools.saas.domain.filesystem.FileData;
import org.telosystools.saas.domain.filesystem.Folder;
import org.telosystools.saas.domain.filesystem.Workspace;
import org.telosystools.saas.exception.FileNotFoundException;
import org.telosystools.saas.exception.FolderNotFoundException;
import org.telosystools.saas.exception.InvalidPathException;
import org.telosystools.saas.exception.ProjectNotFoundException;
import org.telosystools.saas.service.WorkspaceService;

import javax.inject.Inject;

@RestController
@RequestMapping("/projects/{id}/workspace")
public class WorkspaceController {

    @Inject
    private WorkspaceService workspaceService;

    /**
     * Get the project's workspace
     *
     * @param projectId the project id
     * @return the workspace
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<Workspace> getWorkspace(@PathVariable("id") String projectId) {
        try {
            return new ResponseEntity<>(workspaceService.getWorkspace(projectId), HttpStatus.OK);
        } catch (ProjectNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        }
    }

    /* *******************************
       *********** FOLDERS ***********
       ******************************* */
    /**
     * Create a new folder at the specified path
     *
     * @param projectId Project ID
     * @param fileData  folder path as json object
     * @return new Folder
     */
    @RequestMapping(value = "/folders", method = RequestMethod.POST)
    public ResponseEntity<Folder> createFolder(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        if (StringUtils.isEmpty(fileData.getPath())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(workspaceService.createFolder(fileData.getPath(), projectId), HttpStatus.CREATED);
        } catch (FolderNotFoundException | ProjectNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        } catch (InvalidPathException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete the folder with the specified path.
     *
     * @param projectId The project id
     * @param fileData The folder to delete
     * @return OK - 200 if folder has been deleted.
     */
    @RequestMapping(value = "/folders", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteFolder(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        if (StringUtils.isEmpty(fileData.getPath())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            workspaceService.removeFolder(fileData.getPath(), projectId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProjectNotFoundException | FolderNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        } catch (InvalidPathException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Rename the folder with the specified path
     *
     * @param projectId The project id
     * @param fileData The folder to patch
     * @return OK - 200 if folder file has been renamed.
     */
    @RequestMapping(value = "/folders", method = RequestMethod.PATCH)
    public ResponseEntity<Object> renameFolder(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        if (StringUtils.isEmpty(fileData.getPath()) || StringUtils.isEmpty(fileData.getName()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            workspaceService.renameFolder(fileData.getPath(), fileData.getName(), projectId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProjectNotFoundException | FolderNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        } catch (InvalidPathException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.BAD_REQUEST);
        }
    }

    /* *******************************
       ************ FILES ************
       ******************************* */

    /**
     * Create a new file at the specified path.
     *
     * @param projectId The project id
     * @param fileData  The file to add
     * @return a new File if created, error 404 if the parent folder doesn't exist
     */
    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public ResponseEntity<File> createFile(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        try {
            if (StringUtils.isEmpty(fileData.getPath())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(workspaceService.createFile(fileData.getPath(), fileData.getContent(), projectId),
                    HttpStatus.CREATED);
        } catch (FolderNotFoundException | FileNotFoundException | ProjectNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        } catch (InvalidPathException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete the file with the specified path.
     *
     * @param projectId The project id
     * @param fileData The file to delete
     * @return OK - 200 if file has been deleted.
     */
    @RequestMapping(value = "/files", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteFile(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        try {
            if (StringUtils.isEmpty(fileData.getPath())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            workspaceService.removeFile(fileData.getPath(), projectId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProjectNotFoundException | FileNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        } catch (InvalidPathException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Rename the file with the specified path
     *
     * @param projectId The project id
     * @param fileData The file to patch
     * @return OK - 200 if the file has been renamed.
     */
    @RequestMapping(value = "/files", method = RequestMethod.PATCH)
    public ResponseEntity<Object> renameFile(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        if (StringUtils.isEmpty(fileData.getPath()) || StringUtils.isEmpty(fileData.getName()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            workspaceService.renameFile(fileData.getPath(), fileData.getName(), projectId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProjectNotFoundException | FileNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        } catch (InvalidPathException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Return the content of the given file.
     *
     * @param projectId Project ID
     * @param fileData  the file path as a json object
     * @return The file content as a String
     */
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public ResponseEntity<FileData> getFileContent(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        if (StringUtils.isEmpty(fileData.getPath())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(workspaceService.getFileContent(fileData.getPath(), projectId), HttpStatus.OK);
        } catch (FileNotFoundException | ProjectNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update the content of the given file.
     *
     * @param projectId Project ID
     * @param fileData  The file path and content as a Json object
     */
    @RequestMapping(value = "/files", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateFileContent(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        try {
            if (StringUtils.isEmpty(fileData.getPath())|| StringUtils.isEmpty(fileData.getPath()))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            workspaceService.updateFile(fileData.getPath(), fileData.getContent(), projectId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FileNotFoundException | ProjectNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        }
    }

    private HttpHeaders getErrorHttpHeaders(Exception e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("error_message", e.getMessage());
        return responseHeaders;
    }
}