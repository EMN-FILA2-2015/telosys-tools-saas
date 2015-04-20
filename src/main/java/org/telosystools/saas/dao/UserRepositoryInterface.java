package org.telosystools.saas.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.telosystools.saas.domain.User;

import java.util.List;

/**
 * Created by Adrian on 20/04/15.
 */
@Repository
public interface UserRepositoryInterface extends MongoRepository<User, String> {

    User findByEmailAndPassword(String email, String password);

    // {"contributions":projectId}
    List<User> findByContributions(String projectId);

}
