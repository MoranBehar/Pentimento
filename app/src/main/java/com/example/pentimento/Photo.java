package com.example.pentimento;

import android.graphics.Bitmap;

public class Photo {

    private Bitmap photo;
    private String id;

    public Photo() {}

    public Photo(String id) {
        this.id = id;
    }

    public Photo(String id, Bitmap photo) {
        this.photo = photo;
        this.id = id;
    }

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
}
