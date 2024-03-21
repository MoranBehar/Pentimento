package com.example.pentimento;

import java.util.Date;

public class AlbumPhoto {
    private String albumId;
    private String photoId;
    private Date createDate;

    // Constructors
    public AlbumPhoto() {}

    public AlbumPhoto(String albumId, String photoId) {
        this.albumId = albumId;
        this.photoId = photoId;
        this.createDate = new Date();

    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
