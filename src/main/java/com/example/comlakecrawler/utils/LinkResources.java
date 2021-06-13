package com.example.comlakecrawler.utils;

import javax.persistence.*;

@Entity
@Table(name = "sources")
public class LinkResources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    @Column(name = "websites")
    private String websites;
    @Column(name = "topic")
    private String topic;
    @Column(name = "link")
    private String link;

    public LinkResources() {
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getWebsites() {
        return websites;
    }

    public void setWebsites(String websites) {
        this.websites = websites;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
