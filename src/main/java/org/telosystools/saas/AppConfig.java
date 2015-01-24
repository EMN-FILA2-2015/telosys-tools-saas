package org.telosystools.saas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class AppConfig {

  /* Classe de lancement de l'application */
  public static void main(String[] args) {
    SpringApplication.run(AppConfig.class, args);
  }
}
