package org.telosystools.saas.domain.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Adrian on 23/04/15.
 *
 * Sub-document of ProjectConfigurations for folders
 */
public class ProjectConfigFolders {

    @Field("SRC")
    @JsonProperty("SRC")
    private String src;

    @Field("RES")
    @JsonProperty("RES")
    private String res;

    @Field("WEB")
    @JsonProperty("WEB")
    private String web;

    @Field("TEST_SRC")
    @JsonProperty("TEST_SRC")
    private String testSrc;

    @Field("TEST_RES")
    @JsonProperty("TEST_RES")
    private String testRes;

    @Field("DOC")
    @JsonProperty("DOC")
    private String doc;

    @Field("TMP")
    @JsonProperty("TMP")
    private String tmp;

    public ProjectConfigFolders() {
        src = "";
        res = "";
        web = "";
        testSrc = "";
        testRes = "";
        doc = "";
        tmp = "";
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTestSrc() {
        return testSrc;
    }

    public void setTestSrc(String testSrc) {
        this.testSrc = testSrc;
    }

    public String getTestRes() {
        return testRes;
    }

    public void setTestRes(String testRes) {
        this.testRes = testRes;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
}
