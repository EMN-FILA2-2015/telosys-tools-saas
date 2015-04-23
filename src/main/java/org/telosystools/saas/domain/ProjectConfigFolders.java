package org.telosystools.saas.domain;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Adrian on 23/04/15.
 *
 * Sub-document of ProjectConfigurations for folders
 */
public class ProjectConfigFolders {

    @Field("SRC")
    private String src;

    @Field("RES")
    private String res;

    @Field("WEB")
    private String web;

    @Field("TEST_SRC")
    private String test_src;

    @Field("TEST_RES")
    private String test_res;

    @Field("DOC")
    private String doc;

    @Field("TMP")
    private String tmp;

    public ProjectConfigFolders() {
        src = "";
        res = "";
        web = "";
        test_src = "";
        test_res = "";
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

    public String getTest_src() {
        return test_src;
    }

    public void setTest_src(String test_src) {
        this.test_src = test_src;
    }

    public String getTest_res() {
        return test_res;
    }

    public void setTest_res(String test_res) {
        this.test_res = test_res;
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
