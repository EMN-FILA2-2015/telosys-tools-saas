package org.telosystools.saas.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.telosystools.saas.domain.User;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Adrian on 16/04/15.
 */
@Repository
public class UserDao {

    @Autowired
    private MongoTemplate mongoTemplateGeneral;

    public User findByLogin(String email, String password) {
        return mongoTemplateGeneral.findOne(new Query(where("users.email").is(email).and("users.password").is(password)), User.class);
    }

    public void save(User user) {
        mongoTemplateGeneral.save(user);
    }

    public User findById(String userId) {
        return mongoTemplateGeneral.findById(userId, User.class);
    }
}
