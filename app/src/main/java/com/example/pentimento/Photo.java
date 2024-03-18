package com.example.pentimento;

import android.graphics.Bitmap;
import java.util.Date;

public class Photo {

    private Bitmap photo;
    private String id;
    private String title;
    private Date createDate;

    // Constructors
    public Photo() {}

    public Photo(String id) {
        this.id = id;
    }

    public Photo(String id, Bitmap photo) {
        this.photo = photo;
        this.id = id;
    }

    // Getters and setters
    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
