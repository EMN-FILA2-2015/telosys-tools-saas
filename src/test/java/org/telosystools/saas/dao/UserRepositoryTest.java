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
import org.telosystools.saas.domain.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Adrian on 20/04/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Import(MongoConfiguration.class)
public class UserRepositoryTest {

    public static final String USER_1 = "user1";
    public static final String EMAIL_1 = "email1";
    public static final String PASSWORD_1 = "password1";
    public static final String USER_2 = "user2";
    public static final String EMAIL_2 = "email2";
    public static final String PASSWORD_2 = "password2";
    public static final String PROJECT_1 = "project_1";
    public static final String PROJECT_2 = "project_2";

    @Autowired
    private UserRepository userRepository;

    private final List<User> userList = new ArrayList<>();

    @Before
    public void setUp() {
        User u1 = new User(USER_1);
        u1.setEmail(EMAIL_1);
        u1.setPassword(PASSWORD_1);
        u1.addContribution(PROJECT_1);
        u1.addContribution(PROJECT_2);

        User u2 = new User(USER_2);
        u2.setEmail(EMAIL_2);
        u2.setPassword(PASSWORD_2);
        u2.addContribution(PROJECT_1);

        userList.add(u1);
        userList.add(u2);

        userRepository.save(userList);
    }

    @After
    public void tearDown() {
        userRepository.delete(userList);
    }
    
    @Test
    public void testFindByEmailAndPassword() {
        User res = userRepository.findByEmailAndPassword(EMAIL_1, PASSWORD_1);
        assertNotNull(res);
        assertEquals(EMAIL_1, res.getEmail());
        assertEquals(PASSWORD_1, res.getPassword());
        assertEquals(USER_1, res.getLogin());
    }
    
    @Test
    public void testFindByContributions() {
        List<User> res = userRepository.findByContributions(PROJECT_1);
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(USER_1, res.get(0).getLogin());
        assertEquals(USER_2, res.get(1).getLogin());

        res = userRepository.findByContributions(PROJECT_2);
        assertEquals(1, res.size());
        assertEquals(USER_1, res.get(0).getLogin());
    }
}
