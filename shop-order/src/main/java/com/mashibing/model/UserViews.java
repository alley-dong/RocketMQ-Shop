package com.mashibing.model;

public class UserViews {
    private String VIEW_NAME;
    private String TEXT_LENGTH;
    private String TEXT;// clob大字段
    private String EDITIONING_VIEW;
    private String READ_ONLY;

    public String getVIEW_NAME() {
        return VIEW_NAME;
    }

    public void setVIEW_NAME(String VIEW_NAME) {
        this.VIEW_NAME = VIEW_NAME;
    }

    public String getTEXT_LENGTH() {
        return TEXT_LENGTH;
    }

    public void setTEXT_LENGTH(String TEXT_LENGTH) {
        this.TEXT_LENGTH = TEXT_LENGTH;
    }

    public String getTEXT() {
        return TEXT;
    }

    public void setTEXT(String TEXT) {
        this.TEXT = TEXT;
    }

    public String getEDITIONING_VIEW() {
        return EDITIONING_VIEW;
    }

    public void setEDITIONING_VIEW(String EDITIONING_VIEW) {
        this.EDITIONING_VIEW = EDITIONING_VIEW;
    }

    public String getREAD_ONLY() {
        return READ_ONLY;
    }

    public void setREAD_ONLY(String READ_ONLY) {
        this.READ_ONLY = READ_ONLY;
    }
}
