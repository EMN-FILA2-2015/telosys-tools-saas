package org.telosystools.saas;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration.
 */
@Configuration
@EnableMongoRepositories("org.telosystools.saas.dao")
public class MongodbConfiguration {

    public @Bean
    Mongo mongo() throws Exception {
        return new MongoClient();
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "db_general");
    }

}
