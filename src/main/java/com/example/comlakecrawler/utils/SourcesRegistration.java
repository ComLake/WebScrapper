package com.example.comlakecrawler.utils;

public class SourcesRegistration {
    private String topic;
    private boolean kaggle;
    private boolean github;
    private boolean box;
    private boolean dropbox;

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
}
