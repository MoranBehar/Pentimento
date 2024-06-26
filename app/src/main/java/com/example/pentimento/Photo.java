package com.example.pentimento;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class Photo {

    private Bitmap photo;
    private String id;
    private String title;
    private Date createDate;
    private String ownerId;
    private double locationLatitude;
    private double locationLongitude;


    private AlbumsManager albumsManager = AlbumsManager.getInstance();


    // Constructors
    public Photo() {}

    public Photo(String id) {
        this.id = id;
    }

    public Photo(String id, Bitmap photo) {
        this.photo = photo;
        this.id = id;
        this.createDate = new Date();
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public interface actionCallback {
        void onSuccess();
    }

    public void delete(actionCallback callback) {
        // delete the photo file from storage
        StorageManager.getInstance().deletePhoto(getId(),
                new StorageActionResult() {
                    @Override
                    public void onSuccess(Object data) {

                        // 1. delete the photo from all albums
                        removeFromAlbums();

                        // 2. delete the photo from any sharing
                        cancelSharing();

                        // 3. delete the photo from the user (main gallery)
                        deleteFromOwner();

                        // return
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }

    private void removeFromAlbums() {
        DBManager.getInstance().getPhotosAlbums(this, new DBManager.DBActionResult<ArrayList>() {
            @Override
            public void onSuccess(ArrayList data) {
                for (Object albumId : data) {
                    DBManager.getInstance().deletePhotoFromAlbum(getId(), albumId.toString());
//                    DBManager.getInstance().updateAlbumNumOfPhotos(albumId.toString(), -1);
                    albumsManager.setLastUpdatedAlbumId(albumId.toString());
                }
            }

            @Override
            public void onError(Exception e) {
                //if no album was found - we have noting to do
                Log.e("photoDelete", "onError: ");
            }
        });

    }


    private void cancelSharing() {
        DBManager.getInstance().deletePhotoSharing(this);
    }

    private void deleteFromOwner() {
        DBManager.getInstance().deletePhotoFromOwner(this);
    }

    // Override equals() - needed to support removing the object from ArrayList<Photo>
    @Override
    public boolean equals(Object o) {

        // If its the same Photo object its equal
        if (this == o) return true;

        // If its not the same type of object (i.e. photo <> album) return false
        if (o == null || getClass() != o.getClass()) return false;

        // its the same class, check if id are equals
        Photo photo = (Photo) o;

        if ( id != null) {
            // if not null we can compare the ids
            return id.equals(photo.id) ;
        } else {
            // if null = check if null
            return photo.id == null;
        }

    }

    public void load(actionCallback callback) {
        DBManager.getInstance().getPhotoById(this.getId(), new DBManager.DBActionResult<Photo>() {
            @Override
            public void onSuccess(Photo myPhoto) {
                setTitle(myPhoto.getTitle());
                callback.onSuccess();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
