package org.telosystools.saas.service.impl;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

public class ProjectServiceImplTest {


    private ProjectServiceImpl projectService;
    private ProjectRepository repoMock;

    @Before
    public void setUp() throws Exception {
        projectService = new ProjectServiceImpl();
        repoMock = EasyMock.createMock(ProjectRepository.class);
        projectService.setRepository(repoMock);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFind() {
        EasyMock.expect(repoMock.findOne("1")).andReturn(new Project("telosys")).once();
        EasyMock.replay(repoMock);

        Project project = projectService.find("1");

        assertNotNull(project);
        assertEquals("Project telosys", "telosys", project.getName());
        EasyMock.verify(repoMock);
    }

    @Test
    public void testList() {
        List<Project> projects = new ArrayList<Project>();
        projects.add(new Project("telosys"));
        projects.add(new Project("docker"));

        EasyMock.expect(repoMock.findAll()).andReturn(projects).once();

        EasyMock.replay(repoMock);

        List<Project> res = projectService.list();

        assertNotNull(res);
        assertEquals("2 elements", projects.size(), res.size());
        assertEquals("Projet telosys", projects.get(0).getName(), res.get(0).getName());

        EasyMock.verify(repoMock);
    }

    @Test
    public void testInsert() {
        Project project = new Project("telosys");

        EasyMock.expect(repoMock.save(EasyMock.isA(Project.class))).andReturn(project).once();
        EasyMock.replay(repoMock);

        Project res = projectService.insert(new Project("telosys"));

        assertNotNull(project);

        EasyMock.verify(repoMock);

    }

    @Test
    @Ignore
    public void testDelete() {
    }

}