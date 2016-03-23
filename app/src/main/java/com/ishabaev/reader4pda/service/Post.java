package com.ishabaev.reader4pda.service;


import android.net.Uri;

public class Post {
    private String title;
    private String imageUri;
    private String desriptionl;
    private String detailsUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDesriptionl() {
        return desriptionl;
    }

    public void setDesriptionl(String desriptionl) {
        this.desriptionl = desriptionl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
}
