package com.example.comlakecrawler.service.downloader.target;

import com.example.comlakecrawler.service.downloader.CrawlerInterface;

import java.util.ArrayList;

public abstract class Crawler {
    protected CrawlerInterface listener;
    protected ArrayList<String>sources;
    protected String keySeek;
    protected boolean isExit;
    protected static final int BUFFER_SIZE = 4096;

    public Crawler() {
        sources = new ArrayList<>();
    }

    public ArrayList<String> getSources() {
        return sources;
    }

    public void setSources(ArrayList<String> sources) {
        this.sources = sources;
    }

    public String getKeySeek() {
        return keySeek;
    }

    public void setListener(CrawlerInterface listener) {
        this.listener = listener;
    }

    public void setKeySeek(String keySeek) {
        this.keySeek = keySeek;
    }
    public abstract void download(String link, String path);
    public abstract void scrapper();
    protected void onStart(){
        this.isExit = false;
    }
    protected void onStop(){
        isExit = true;
    }
    public void saveLinks(){
        if (listener!=null){
            listener.updateSources(sources);
        }
    }
}
