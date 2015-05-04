package org.telosystools.saas.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.telosystools.saas.Application;
import org.telosystools.saas.config.MongoConfiguration;
import org.telosystools.saas.domain.filesystem.File;
import org.telosystools.saas.domain.filesystem.Workspace;
import org.telosystools.saas.domain.project.Project;
import org.telosystools.saas.domain.project.ProjectConfiguration;
import org.telosystools.saas.exception.ProjectNotFoundException;
import org.telosystools.saas.exception.UserNotFoundException;
import org.telosystools.saas.service.ProjectService;
import org.telosystools.saas.service.WorkspaceService;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Import(MongoConfiguration.class)
public class ProjectControllerTest {

    private ObjectMapper mapper;

    @Autowired
    private ProjectController projectController;

    private ProjectService projectService;

    private WorkspaceService workspaceService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
        final Field serviceField = ProjectController.class.getDeclaredField("projectService");
        serviceField.setAccessible(true);
        projectService = (ProjectService) serviceField.get(projectController);
        final Field wsServiceField = ProjectController.class.getDeclaredField("workspaceService");
        wsServiceField.setAccessible(true);
        workspaceService = (WorkspaceService) wsServiceField.get(projectController);
        mapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws NoSuchFieldException, IllegalAccessException, UserNotFoundException {
        List<Project> projects = projectService.findAllByUser();
        projects.forEach(e -> projectService.deleteProject(e.getId()));
    }

    /*
    getProject : récupère un projet par son ID -> projet OK
     */
    @Test
    public void testGetProject() throws Exception {
        // Given
        Project project = new Project();
        project.setName("Mon projet");
        Project expectedProject = projectService.createProject(project);
        String projectID = expectedProject.getId();

        // When
        MvcResult mvcResult = this.mockMvc.perform(get("/projects/" + projectID)).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn();

        // Then
        String jsonContent = mvcResult.getResponse().getContentAsString();
        Project actualProject = mapper.readValue(jsonContent, Project.class);

        assertEquals(expectedProject.getId(),actualProject.getId());
        assertEquals(expectedProject.getName(),actualProject.getName());
    }

    @Test
    public void testGetProject_NotFound() throws Exception {
        // Given
        String projectID = ObjectId.get().toString();

        // When
        this.mockMvc.perform(get("/projects/" + projectID))

        // Then
                .andExpect(status().isNotFound());
    }

    /*
    getAllProjects : récupère tous les projets pour un utilisateur donné -> bon nb de projets,
    */
    @Test
    public void testGetAllProjects() throws Exception {
        // Given
        Project project1 = new Project();
        project1.setName("Project1");
        projectService.createProject(project1);
        Project project2 = new Project();
        project2.setName("Project2");
        projectService.createProject(project2);

        // When
        MvcResult mvcResult = this.mockMvc.perform(get("/projects/"))

        // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        String jsonContent = mvcResult.getResponse().getContentAsString();
        List projects = mapper.readValue(jsonContent, List.class);
        assertEquals(2,projects.size());
    }

    /*
    createProject : crée un projet et le récupère -> status CREATED
    */
    @Test
    public void testCreateProject() throws Exception {
        // Given
        String projectName = "Nouveau Projet";

        // When
        MvcResult mvcResult = this.mockMvc.perform(post("/projects/").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"" + projectName + "\"}"))
        // Then
                .andExpect(status().isCreated())
                .andReturn();

        String jsonContent = mvcResult.getResponse().getContentAsString();
        Project createdProject = mapper.readValue(jsonContent, Project.class);
        assertEquals(projectName,createdProject.getName());
    }

    @Test
    public void testCreateProject_Conflict() throws Exception {
        // Given
        Project project = new Project();
        String projectName = "Mon projet";
        project.setName(projectName);
        projectService.createProject(project);

        // When
        this.mockMvc.perform(post("/projects/").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"" + projectName + "\"}"))

        // Then
            .andExpect(status().isConflict());
    }

    /*
    deleteProject : supprime un projet -> status OK
    */
    @Test(expected = ProjectNotFoundException.class)
    public void testDeleteProject() throws Exception {
        // Given
        Project project = new Project();
        project.setName("Mon projet");
        Project expectedProject = projectService.createProject(project);
        String projectID = expectedProject.getId();

        // When
        this.mockMvc.perform(delete("/projects/" + projectID))
        // Then
                .andExpect(status().isOk())
                .andReturn();

        // Expect an exception
        projectService.loadProject(projectID);
    }

    /*
    getWorkspace : récupère l'arborescence d'un projet -> contenu json
    */
    @Test
    public void testGetWorkspace() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();

        // When
        MvcResult mvcResult = this.mockMvc.perform(get("/projects/" + projectID + "/workspace"))

        // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        String jsonContent = mvcResult.getResponse().getContentAsString();
        Workspace workspace = mapper.readValue(jsonContent, Workspace.class);
        assertNotNull(workspace);
    }

    @Test
    public void testGetWorkspace_NotFound() throws Exception {
        // Given
        String projectID = ObjectId.get().toString();

        // When
        this.mockMvc.perform(get("/projects/" + projectID + "/workspace"))

        // Then
                .andExpect(status().isNotFound());
    }

    /*
    createFile : crée un fichier -> fichier créé status created
    */
    @Test
    public void testCreateFile() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();
        String filePath = "models/model_1.xml";
        String fileData = "{\"path\":\"" + filePath + "\", \"content\":\"Contenu du fichier\"}";

        // When
        MvcResult mvcResult = this.mockMvc.perform(post("/projects/"+projectID+"/workspace/files").contentType(MediaType.APPLICATION_JSON).content(fileData))

        // Then
                .andExpect(status().isCreated())
                .andReturn();

        String jsonContent = mvcResult.getResponse().getContentAsString();
        File file = mapper.readValue(jsonContent, File.class);
        assertNotNull(file);
        assertEquals(filePath,file.getAbsolutePath().replace('*','.'));
    }

    @Test
    public void testCreateFile_ProjectNotFound() throws Exception {
        // Given
        String projectID = ObjectId.get().toString();

        String filePath = "models/model_1.xml";
        String fileData = "{\"path\":\"" + filePath + "\", \"content\":\"Contenu du fichier\"}";

        // When
        this.mockMvc.perform(post("/projects/"+projectID+"/workspace/files").contentType(MediaType.APPLICATION_JSON).content(fileData))

        // Then
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateFile_FolderNotFound() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();
        String filePath = "models/folder1/model_1.xml";
        String fileData = "{\"path\":\"" + filePath + "\", \"content\":\"Contenu du fichier\"}";

        // When
        this.mockMvc.perform(post("/projects/"+projectID+"/workspace/files").contentType(MediaType.APPLICATION_JSON).content(fileData))

        // Then
                .andExpect(status().isNotFound());
    }

    /*
    getFileContent : récupère le contenu d'un fichier -> retourne une chaîne de caractère status ok
    */
    @Test
    public void testGetFileContent() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();
        String expectedContent = "Contenu du fichier";
        String fileID = workspaceService.createFile("models/model_2.xml", expectedContent, projectID).getGridFSId();

        // When
        MvcResult mvcResult = this.mockMvc.perform(get("/projects/" + projectID + "/workspace/files/" + fileID))

        // Then
                .andExpect(status().isOk())
                .andReturn();

        String actualContent = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testGetFileContent_NotFound() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();
        String fileID = ObjectId.get().toString();

        // When
        this.mockMvc.perform(get("/projects/" + projectID + "/workspace/files/" + fileID))

        // Then
                .andExpect(status().isNotFound());
    }

    /*
    updateFile : change le contenu d'un fichier -> status ok
    */
    @Test
    public void testUpdateFile() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();

        String filePath = "models/model_2.xml";

        File oldFile = workspaceService.createFile(filePath, "", projectID);

        String fileContent = "content";
        String fileData = "{\"path\":\"" + filePath + "\", \"content\":\"" + fileContent + "\"}";

        // When
        final String url = "/projects/" + projectID + "/workspace/files/";
        String jsonContent =  this.mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(fileData))
        // Then
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        File file = mapper.readValue(jsonContent, File.class);

        assertEquals(file.getAbsolutePath(), oldFile.getAbsolutePath());
        assertNotEquals(file.getGridFSId(), oldFile.getGridFSId());
        assertEquals(fileContent, workspaceService.getFileContent(projectID, file.getGridFSId()));

    }


    @Test
    public void testUpdateFile_ProjectNotFound() throws Exception {
        // Given
        String projectID = ObjectId.get().toString();

        String filePath = "models/model_2.xml";

        String fileContent = "content";
        String fileData = "{\"path\":\"" + filePath + "\", \"content\":\"" + fileContent + "\"}";

        // When
        final String url = "/projects/" + projectID + "/workspace/files/";
        this.mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(fileData))

        // Then
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateFile_FileNotFound() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();

        String filePath = "models/model_2.xml";

        String fileContent = "content";
        String fileData = "{\"path\":\"" + filePath + "\", \"content\":\"" + fileContent + "\"}";

        // When
        final String url = "/projects/" + projectID + "/workspace/files/";
        this.mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(fileData))

        // Then
                .andExpect(status().isNotFound());
    }

    /*
    setProjectConfig : change la config du projet -> status created
    */
    @Test
    public void testSetProjectConfig() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();
        String config = "{\"packages\":{\"ROOT_PKG\":\"rootPkgVal\",\"ENTITY_PKG\":\"entityPkgVal\"},\"folders\":{\"SRC\":\"srcVal\",\"RES\":\"resVal\",\"WEB\":\"webVal\",\"TEST_SRC\":\"test_srcVal\",\"TEST_RES\":\"test_resVal\",\"DOC\":\"docVal\",\"TMP\":\"tmpVal\"},\"variables\":{\"VAR1\":\"val1\"}}";

        // When
        this.mockMvc.perform(post("/projects/"+projectID+ "/config/telosystoolscfg").contentType(MediaType.APPLICATION_JSON).content(config))
        // Then
                .andExpect(status().isOk());


        project = projectService.loadProject(projectID);
        assertEquals("rootPkgVal", project.getProjectConfiguration().getPackages().getRootPkg());
        assertEquals("entityPkgVal", project.getProjectConfiguration().getPackages().getEntityPkg());
        assertEquals("val1", project.getProjectConfiguration().getVariables().get("VAR1"));
        assertEquals("test_resVal", project.getProjectConfiguration().getFolders().getTestRes());
    }

    @Test
    public void testSetProjectConfiguration_ProjectNotFound() throws Exception {
        // Given
        String projectID = ObjectId.get().toString();
        String config = "{\"packages\":{\"ROOT_PKG\":\"rootPkgVal\",\"ENTITY_PKG\":\"entityPkgVal\"},\"folders\":{\"SRC\":\"srcVal\",\"RES\":\"resVal\",\"WEB\":\"webVal\",\"TEST_SRC\":\"test_srcVal\",\"TEST_RES\":\"test_resVal\",\"DOC\":\"docVal\",\"TMP\":\"tmpVal\"},\"variables\":{\"VAR1\":\"val1\"}}";

        // When
        this.mockMvc.perform(post("/projects/" + projectID + "/config/telosystoolscfg").contentType(MediaType.APPLICATION_JSON).content(config))

        // Then
                .andExpect(status().isNotFound());
    }

    /*
    getProjectConfiguration : récupère la configuration d'un projet -> status Ok
    */
    @Test
    public void testGetProjectConfiguration() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();

        ProjectConfiguration cfg = new ProjectConfiguration();
        cfg.getFolders().setDoc("docVal");
        cfg.getPackages().setEntityPkg("entityVal");
        cfg.getVariables().put("var", "val");
        projectService.updateProjectConfig(projectID, cfg);

        // When
        MvcResult mvcResult = this.mockMvc.perform(get("/projects/" + projectID + "/config/telosystoolscfg"))

        // Then
                .andExpect(status().isOk())
                .andReturn();
        String jsonContent = mvcResult.getResponse().getContentAsString();
        ProjectConfiguration projectConfiguration = mapper.readValue(jsonContent, ProjectConfiguration.class);

        // Asserts
        assertNotNull(projectConfiguration);
        assertEquals(projectConfiguration.getFolders().getDoc(), cfg.getFolders().getDoc());
        assertEquals(projectConfiguration.getPackages().getEntityPkg(), cfg.getPackages().getEntityPkg());
        assertEquals(projectConfiguration.getVariables().get("var"), cfg.getVariables().get("var"));
    }

    @Test
    public void testGetProjectConfiguration_ProjectNotFound() throws Exception {
        // Given
        String projectID = ObjectId.get().toString();

        // When
        this.mockMvc.perform(get("/projects/" + projectID + "/config/telosystoolscfg"))

                // Then
                .andExpect(status().isNotFound());
    }
}