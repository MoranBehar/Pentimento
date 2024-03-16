package com.example.pentimento;

import java.util.Date;

public class Album {

    private String id;
    private String ownerId;
    private String title;
    private Date createDate;

    public Album(String ownerId, String title) {
        this.ownerId = ownerId;
        this.title = title;
        this.createDate = new Date();
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
