package com.ggenius.whattowear.model;

public class Clothes {

    int id;
    String title, info;

    public Clothes(int id, String title, String info) {
        this.id = id;
        this.title = title;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String info) {
        this.title = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
