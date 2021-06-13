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
    @Column(name = "author")
    private String author;
    @Column(name = "name_dataset")
    private String name_dataset;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName_dataset() {
        return name_dataset;
    }

    public void setName_dataset(String name_dataset) {
        this.name_dataset = name_dataset;
    }
}
