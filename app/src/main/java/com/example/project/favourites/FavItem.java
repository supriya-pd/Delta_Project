package com.example.project.favourites;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_table")
public class FavItem {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    private String title;
    private String publishedAt;
    private String UrlToImage;
    private String Url;

    public FavItem(String title, String publishedAt, String UrlToImage, String Url) {
        this.title = title;
        this.publishedAt = publishedAt;
       this.UrlToImage = UrlToImage;
        this.Url = Url;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrlToImage() {
        return UrlToImage;
    }

    public String getUrl() {
        return Url;
    }



    }

