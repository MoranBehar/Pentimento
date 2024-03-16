package com.example.pentimento;

import android.graphics.Bitmap;

public class SharedPhoto extends Photo {
    private User sharedBy;
    private String sharedOn;

    public SharedPhoto() {
        super();
    }
    public SharedPhoto(String id, User sharedBy, String sharedOn) {
        super(id);
        this.sharedBy = sharedBy;
        this.sharedOn = sharedOn;
    }

    public SharedPhoto(String id, Bitmap photo, User sharedBy, String sharedOn) {
        super(id,photo);
        this.sharedBy = sharedBy;
        this.sharedOn = sharedOn;
    }

    public User getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(User sharedBy) {
        this.sharedBy = sharedBy;
    }

    public String getSharedOn() {
        return sharedOn;
    }

    public void setSharedOn(String sharedOn) {
        this.sharedOn = sharedOn;
    }
}
