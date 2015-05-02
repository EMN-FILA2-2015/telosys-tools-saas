package org.telosystools.saas.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.telosystools.saas.Application;
import org.telosystools.saas.config.MongoConfiguration;
import org.telosystools.saas.domain.project.DefaultVariables;
import org.telosystools.saas.domain.project.Project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Adrian on 20/04/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Import(MongoConfiguration.class)
public class ProjectRepositoryTest {

    public static final String OWNER = "Tester";
    public static final String P_1 = "P1";
    public static final String P_2 = "P2";

    @Autowired
    private ProjectRepository projectRepository;

    private List<Project> projectList;

    @Before
    public void setUp() {
        Project p1 = new Project();
        Project p2 = new Project();
        p1.setName(P_1);
        p2.setName(P_2);
        p1.setOwner(OWNER);
        p2.setOwner(OWNER);

        projectList = new ArrayList<>();
        projectList.add(p1);
        projectList.add(p2);

        projectRepository.save(projectList);
    }

    @After
    public void tearDown() {
        projectRepository.delete(projectList);
    }

    @Test
    public void testFindByOwner() {
        List<Project> res = projectRepository.findByOwner(OWNER);
        assertNotNull(res);
        assertEquals(projectList.size(), res.size());
        Project project = res.get(0);
        assertEquals(project.getDefaultVariables().downloadsFolder, (new DefaultVariables()).downloadsFolder);
        assertEquals(project.getDefaultVariables().repositoriesFolder, (new DefaultVariables()).repositoriesFolder);
        assertEquals(project.getDefaultVariables().templatesFolder, (new DefaultVariables()).templatesFolder);
    }

    @Test
    public void testFindManyById() {
        List<String> ids = new ArrayList<>();
        projectList.forEach(e -> ids.add(e.getId()));

        final Iterable<Project> res = projectRepository.findAll(ids);
        Iterator<Project> it = res.iterator();
        int i = 0;
        while (it.hasNext()) {
            assertEquals(projectList.get(i++).getId(), it.next().getId());
        }
    }

    @Test
    public void testFindByOwnerAndName() {
        assertEquals(projectList.get(0).getId(), projectRepository.findByOwnerAndName(OWNER, P_1).get(0).getId());
    }

}