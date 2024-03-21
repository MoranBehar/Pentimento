package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.util.Date;

public class Album {

    private String id;
    private String ownerId;
    private String title;
    private Date createDate;
    private int numOfPhotos;
    private String albumCoverId;

    // 1 - user album
    // 2 - favorites
    private int type;

    // Constructors
    public Album () {}

    public Album(String ownerId, String title) {
        this.ownerId = ownerId;
        this.title = title;
        this.type = 1;
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

    public Date getCreateDate() { return createDate; }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getNumOfPhotos() {
        return numOfPhotos;
    }

    public void setNumOfPhotos(int numOfPhotos) {
        this.numOfPhotos = numOfPhotos;
    }

    public String getAlbumCoverId() {
        return albumCoverId;
    }

    public void setAlbumCoverId(String albumCoverId) {
        this.albumCoverId = albumCoverId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public interface getCoverImageResult {
        void onImageReady(Bitmap bitmap);
    }

    public void getCoverImage(getCoverImageResult callback) {
        StorageManager.getInstance().getImageById(getAlbumCoverId(), new StorageActionResult<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                callback.onImageReady(bitmap);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void removePhoto(Photo photoToRemove) {
        DBManager.getInstance().deletePhotoFromAlbum(photoToRemove, this);
    }
}
