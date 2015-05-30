package org.telosystools.saas;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.telosystools.saas.domain.filesystem.File;

@Component
@Profile("dev")
public class DevApplication extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File file = new File("yo");

        String absolutePath;
        if(file.getAbsolutePath().indexOf('/') == 0) {
            absolutePath = "file://" + file.getAbsolutePath();
        } else {
            absolutePath = "file:///" + file.getAbsolutePath();
        }

        String client = absolutePath+"/client/";
        String tmp = absolutePath+"/.tmp/";

        registry.addResourceHandler("/**")
                .addResourceLocations(client, tmp)
                .setCachePeriod(0)
                .resourceChain(false);
    }
}
