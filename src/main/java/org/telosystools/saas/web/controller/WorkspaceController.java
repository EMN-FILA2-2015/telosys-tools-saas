package org.telosystools.saas.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.telosystools.saas.domain.filesystem.FileData;
import org.telosystools.saas.domain.filesystem.RootFolder;
import org.telosystools.saas.domain.filesystem.Workspace;
import org.telosystools.saas.exception.*;
import org.telosystools.saas.service.WorkspaceService;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/projects/{id}/workspace")
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
     * @return updated RootFolder
     */
    @RequestMapping(value = "/folders", method = RequestMethod.POST)
    public ResponseEntity<RootFolder> createFolder(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        if (StringUtils.isEmpty(fileData.getPath())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(workspaceService.createFolder(fileData.getPath(), projectId), HttpStatus.CREATED);
        } catch (FolderNotFoundException | ProjectNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        } catch (InvalidPathException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.BAD_REQUEST);
        } catch (DuplicateResourceException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.CONFLICT);
        }
    }

    /**
     * Delete the folder with the specified path.
     *
     * @param projectId The project id
     * @param path The path of the folder to delete
     * @return Updated rootFolder, OK - 200 if folder has been deleted.
     */
    @RequestMapping(value = "/folders", method = RequestMethod.DELETE)
    public ResponseEntity<RootFolder> deleteFolder(@PathVariable("id") String projectId,  @RequestParam("path") String path) {
        if (StringUtils.isEmpty(path)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(workspaceService.removeFolder(path, projectId), HttpStatus.OK);
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
     * @return updated RootFolder, OK - 200 if folder file has been renamed.
     */
    @RequestMapping(value = "/folders", method = RequestMethod.PATCH)
    public ResponseEntity<RootFolder> renameFolder(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        if (StringUtils.isEmpty(fileData.getPath()) || StringUtils.isEmpty(fileData.getName()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(workspaceService.renameFolder(fileData.getPath(), fileData.getName(), projectId), HttpStatus.OK);
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
     * @return the updated RootFolder if created, error 404 if the parent folder doesn't exist
     */
    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public ResponseEntity<RootFolder> createFile(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        try {
            if (StringUtils.isEmpty(fileData.getPath())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(workspaceService.createFile(fileData.getPath(), fileData.getContent(), projectId),
                    HttpStatus.CREATED);
        } catch (FolderNotFoundException | FileNotFoundException | ProjectNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        } catch (InvalidPathException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.BAD_REQUEST);
        } catch (DuplicateResourceException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.CONFLICT);
        }
    }

    /**
     * Delete the file with the specified path.
     *
     * @param projectId The project id
     * @param path The path to the file to delete
     * @return The updated rootFolder, OK - 200 if file has been deleted.
     */
    @RequestMapping(value = "/files", method = RequestMethod.DELETE)
    public ResponseEntity<RootFolder> deleteFile(@PathVariable("id") String projectId, @RequestParam("path") String path) {
        try {
            if (StringUtils.isEmpty(path)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(workspaceService.removeFile(path, projectId), HttpStatus.OK);
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
     * @return Updated rootfolder, OK - 200 if the file has been renamed.
     */
    @RequestMapping(value = "/files", method = RequestMethod.PATCH)
    public ResponseEntity<RootFolder> renameFile(@PathVariable("id") String projectId, @RequestBody FileData fileData) {
        if (StringUtils.isEmpty(fileData.getPath()) || StringUtils.isEmpty(fileData.getName()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(workspaceService.renameFile(fileData.getPath(), fileData.getName(), projectId), HttpStatus.OK);
        } catch (ProjectNotFoundException | FileNotFoundException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.NOT_FOUND);
        } catch (InvalidPathException e) {
            return new ResponseEntity<>(this.getErrorHttpHeaders(e), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Return the content of the given file.
     * We have to look for the path by parsing the request uri.
     *
     * @param projectId Project ID
     * @param path the file path
     * @return The file content as a String
     */
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public ResponseEntity<FileData> getFileContent(@PathVariable("id") String projectId, @RequestParam("path") String path) {
        if (StringUtils.isEmpty(path)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(workspaceService.getFileContent(path, projectId), HttpStatus.OK);
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