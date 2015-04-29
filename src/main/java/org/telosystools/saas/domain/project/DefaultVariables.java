package org.telosystools.saas.domain.project;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Adrian on 29/04/15.
 * <p/>
 * Variables par défaut (lecture seule)
 */
public class DefaultVariables {

    @Field("DownloadsFolder")
    public final String downloadsFolder = "TelosysTools/downloads";

    @Field("TemplatesFolder")
    public final String templatesFolder = "TelosysTools/templates";

    @Field("RepositoriesFolder")
    public final String repositoriesFolder = "TelosysTools";
}
