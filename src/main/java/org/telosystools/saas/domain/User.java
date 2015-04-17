package org.telosystools.saas.domain;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Created by Adrian on 16/04/15.
 */
@Document(collection = "users")
public class User {

    @Id
    private String login;

    private String email;

    private String password;

    private Set<String> contributions;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {}

    public User(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getContributions() {
        return contributions;
    }

    public void addContribution(String projectId) {
        contributions.add(projectId);
    }

}
