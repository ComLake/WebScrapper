package com.example.comlakecrawler.utils;

public class SourcesRegistration {
    private String topic;
    private boolean kaggle;
    private boolean github;
    private boolean box;
    private boolean dropbox;
    private String linkSharedDbx;
    private String linkSharedBox;
    private String linkSharedDrive;

    public SourcesRegistration() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isKaggle() {
        return kaggle;
    }

    public void setKaggle(boolean kaggle) {
        this.kaggle = kaggle;
    }

    public boolean isGithub() {
        return github;
    }

    public void setGithub(boolean github) {
        this.github = github;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public boolean isDropbox() {
        return dropbox;
    }

    public void setDropbox(boolean dropbox) {
        this.dropbox = dropbox;
    }

    public String getLinkSharedDbx() {
        return linkSharedDbx;
    }

    public void setLinkSharedDbx(String linkSharedDbx) {
        this.linkSharedDbx = linkSharedDbx;
    }

    public String getLinkSharedBox() {
        return linkSharedBox;
    }

    public void setLinkSharedBox(String linkSharedBox) {
        this.linkSharedBox = linkSharedBox;
    }

    public String getLinkSharedDrive() {
        return linkSharedDrive;
    }

    public void setLinkSharedDrive(String linkSharedDrive) {
        this.linkSharedDrive = linkSharedDrive;
    }
}
