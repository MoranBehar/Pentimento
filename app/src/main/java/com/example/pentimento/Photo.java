package com.example.pentimento;

import android.graphics.Bitmap;

public class Photo {

    private Bitmap photo;

    public Photo(Bitmap photo) {
        this.photo = photo;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
