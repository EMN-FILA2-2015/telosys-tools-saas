package org.telosystools.saas.domain;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Adrian on 23/04/15.
 *
 * Sub-document of ProjectConfigurations for packages
 */
public class ProjectConfigPkg {

    @Field("ROOT_PKG")
    private String rootPkg;

    @Field("ENTITY_PKG")
    private String entityPkg;

    public ProjectConfigPkg() {
        rootPkg = "";
        entityPkg = "";
    }

    public String getRootPkg() {
        return rootPkg;
    }

    public void setRootPkg(String rootPkg) {
        this.rootPkg = rootPkg;
    }

    public String getEntityPkg() {
        return entityPkg;
    }

    public void setEntityPkg(String entityPkg) {
        this.entityPkg = entityPkg;
    }
}
