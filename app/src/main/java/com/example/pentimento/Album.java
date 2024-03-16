package com.example.pentimento;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Album {

    private String id;
    private String ownerId;
    private String title;
    private String createDate;

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public Album(String ownerId, String title) {
        this.ownerId = ownerId;
        this.title = title;
        this.createDate = getCurrentDate();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
