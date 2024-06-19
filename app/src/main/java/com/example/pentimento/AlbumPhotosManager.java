package com.example.pentimento;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class AlbumPhotosManager extends BasePhotoManager {

    private static String TAG = "AlbumPhotosManager";
    private static AlbumPhotosManager instance;
    private Album currentAlbum, newAlbum;

    private AlbumsManager albumsManager = AlbumsManager.getInstance();

    // Implement Singleton
    private AlbumPhotosManager() {
        super();
    }

    public static AlbumPhotosManager getInstance() {
        if (instance == null) {
            synchronized (GalleryManager.class) {
                if (instance == null) {
                    instance = new AlbumPhotosManager();
                }
            }
        }
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    public void loadPhotos() {
        if (reloadNeeded()) {
            loadFromDB();
        }
    }

    private void loadFromDB() {
        cleanGallery();
        CollectionReference colRef = fbDB.collection("AlbumPhotos");
        colRef.whereEqualTo("albumId", newAlbum.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> row = document.getData();
                                String pId = row.get("photoId").toString();

                                Photo photo = document.toObject(Photo.class);
                                photo.setId(pId);
                                getImageById(photo);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private boolean reloadNeeded() {

        // If its the first album, load the album's photo
        if ((newAlbum != null) && (currentAlbum == null)) {
            currentAlbum = newAlbum;
            return true;
        }

        // If its not the first album, reload only if the album requested is not in memory
        if ((newAlbum != null) && (currentAlbum != null)) {
            if (!newAlbum.getId().equals(currentAlbum.getId())) {
                currentAlbum = newAlbum;
                return true;
            }
        }


        // If album's data has updated
        if((currentAlbum != null) && (currentAlbum.getId().equals(albumsManager.getLastUpdatedAlbumId())))
        {
            albumsManager.setLastUpdatedAlbumId(null);
            return true;
        }

        // Its the same album - no need to reload
        return false;
    }

    public void setAlbum(String albumId) {
        newAlbum = AlbumsManager.getInstance().getAlbumById(albumId);
        loadPhotos();
    }

    public void reloadAlbum(Album album) {
        if (currentAlbum != null && album.getId().equals(currentAlbum.getId())) {
            loadFromDB();
        }
    }
}
