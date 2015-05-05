package org.telosystools.saas.config;

import com.mongodb.Mongo;
import org.mongeez.MongeezRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Mongeez : Data change set in MongoDB.
 */
@Configuration
public class MongeezConfiguration {

    @Autowired
    private Mongo mongo;

    @Value("${mongeez.migrate}")
    private boolean executeEnabled;

    @Value("${spring.data.mongodb.database}")
    private String dbName;

    @Value("classpath:config/mongeez/mongeez.xml")
    private Resource file;

    @Bean
    public MongeezRunner getMongeez() {
        MongeezRunner mongeez = new MongeezRunner();
        mongeez.setMongo(mongo);
        mongeez.setExecuteEnabled(executeEnabled);
        mongeez.setDbName(dbName);
        mongeez.setFile(file);
        return mongeez;
    }

}
