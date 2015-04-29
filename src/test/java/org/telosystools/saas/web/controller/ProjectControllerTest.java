package org.telosystools.saas.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.telosystools.saas.domain.File;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.domain.ProjectConfiguration;
import org.telosystools.saas.domain.Workspace;
import org.telosystools.saas.exception.UserNotFoundException;
import org.telosystools.saas.service.ProjectService;
import org.telosystools.saas.service.WorkspaceService;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

    /*
    deleteProject : supprime un projet -> status OK
    */
    @Test
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

        Project nullProject = projectService.loadProject(projectID);
        assertNull(nullProject);
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

    /*
    updateFileContent : change le contenu d'un fichier -> status ok
    */
    @Test
    public void testUpdateFileContent() throws Exception {
        // Given
        Project project = new Project();
        project.setName("New Project");
        String projectID = projectService.createProject(project).getId();
        String fileID = workspaceService.createFile("models/model_2.xml", "", projectID).getGridFSId();
        String fileContent = "Contenu du fichier";

        // When
        this.mockMvc.perform(put("/projects/" + projectID + "/workspace/files/" + fileID).content(fileContent))
        // Then
                .andExpect(status().isOk());

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
        String config = "{\"packages\":{\"rootPkg\":\"\",\"entityPkg\":\"\"},\"folders\":{\"src\":\"\",\"res\":\"\",\"web\":\"\",\"test_src\":\"\",\"test_res\":\"\",\"doc\":\"\",\"tmp\":\"\"},\"variables\":{}}";

        // When
        this.mockMvc.perform(post("/projects/"+projectID+ "/config/telosystoolscfg").contentType(MediaType.APPLICATION_JSON).content(config))
        // Then
                .andExpect(status().isCreated());
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

        // When
        MvcResult mvcResult = this.mockMvc.perform(get("/projects/" + projectID + "/config/telosystoolscfg"))

        // Then
                .andExpect(status().isOk())
                .andReturn();
        String jsonContent = mvcResult.getResponse().getContentAsString();
        ProjectConfiguration projectConfiguration = mapper.readValue(jsonContent, ProjectConfiguration.class);
        assertNotNull(projectConfiguration);
    }
}