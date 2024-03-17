package com.example.pentimento;

public class AlbumPhoto {
    private String albumId;
    private String photoId;

    public AlbumPhoto(String albumId, String photoId) {
        this.albumId = albumId;
        this.photoId = photoId;
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
}
