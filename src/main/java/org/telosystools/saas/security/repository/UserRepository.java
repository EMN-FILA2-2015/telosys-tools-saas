package org.telosystools.saas.security.repository;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.telosystools.saas.security.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the User entity.
 */
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(DateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    void delete(User t);

    User findByEmailAndPassword(String email, String password);

    /**
     * Retourne tous les utilisateurs contributeurs du projet.
     *
     * @param projectId l'id du projet
     * @return Liste de contributeurs
     */
    List<User> findByContributions(String projectId);

}
