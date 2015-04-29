package org.telosystools.saas.domain;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Adrian on 23/04/15.
 *
 * Variables fixées pour la configuration de la génération
 */
public class ProjectConfigGen {

    @Field("DownloadsFolder")
    private final String downloadsFolder = "TelosysTools/downloads";

    @Field("TemplatesFolder")
    private final String templatesFolder = "TelosysTools/templates";

    @Field("RepositoriesFolder")
    private final String repositoriesFolder = "TelosysTools";

    public ProjectConfigGen() {}

    public String getDownloadsFolder() {
        return downloadsFolder;
    }

    public String getTemplatesFolder() {
        return templatesFolder;
    }

    public String getRepositoriesFolder() {
        return repositoriesFolder;
    }
}
