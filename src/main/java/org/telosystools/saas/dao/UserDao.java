package org.telosystools.saas.dao;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.telosystools.saas.domain.User;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Adrian on 16/04/15.
 */
@Repository
public class UserDao {

    @Autowired
    private Mongo mongo;

    @Autowired
    private MongoTemplate mongoTemplateGeneral;

    public User findAuthenticate(String login, String password) {
        return mongoTemplateGeneral.findOne(new Query(where("_id").is(login).and("password").is(password)), User.class);
    }

    public void save(User user) {
        mongoTemplateGeneral.save(user);
    }

    public User findByLogin(String login) {
        return mongoTemplateGeneral.findById(login, User.class);
    }

    public List<User> listContributors(String projectId) {
        return mongoTemplateGeneral.find(new Query(where("contributions").in(projectId)), User.class);
    }
}
