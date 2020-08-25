package com.example.project.model;

public class Blog {

    public String title;
    public String desc;
    public String image;
    public String timestamp;
    public String userId;
    String key;
    String username;

    public Blog() {
    }

    public Blog(String title, String desc, String image, String timestamp, String userId,String key,String username) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.timestamp = timestamp;
        this.userId = userId;
        this.key=key;
        this.username=username;
    }

    public String getUsername(){
        return username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
