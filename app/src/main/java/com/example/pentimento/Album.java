package com.example.pentimento;

import java.util.Date;

public class Album {

    private String userId;
    private String title;

    //TODO - organize the album by date
    private Date creatingDate;

    public Album(String userId, String title) {
        this.userId = userId;
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}