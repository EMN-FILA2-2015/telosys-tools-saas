package org.telosystools.saas.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.telosystools.saas.Application;
import org.telosystools.saas.MongodbConfiguration;
import org.telosystools.saas.dao.UserDao;
import org.telosystools.saas.domain.User;

/**
 * Created by Adrian on 29/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Import(MongodbConfiguration.class)
public class ProjectServiceImplIntegrationTest {

    public static final String MAIL = "mail@gmail.com";

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private UserDao userDao;

    @Test
    public void testIntegration() {
        User user = new User("testeur");
        user.setEmail(MAIL);
        user.setPassword("password");

        userDao.save(user);
        assertEquals(user.getLogin(), userDao.findAuthenticate("testeur", "password").getLogin());
    }
}