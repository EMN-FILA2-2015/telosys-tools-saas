package org.telosystools.saas.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.telosystools.saas.domain.User;

import java.util.List;

/**
 * Created by Adrian on 20/04/15.
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmailAndPassword(String email, String password);

    /**
     * Retourne tous les utilisateurs contributeurs du projet.
     *
     * @param projectId l'id du projet
     * @return Liste de contributeurs
     */
    List<User> findByContributions(String projectId);

}
