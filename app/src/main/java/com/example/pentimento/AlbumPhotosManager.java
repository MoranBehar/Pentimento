package com.example.pentimento;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AlbumPhotosManager extends BasePhotoManager {

    private static String TAG = "AlbumPhotosManager";
    private static AlbumPhotosManager instance;
    private Album currentAlbum, newAlbum;

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

    protected void loadPhotos() {

        if (reloadNeeded()) {
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
                                    createItem(row.get("photoId").toString());
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }

    }

    protected void createItem(String imageId) {
        Photo photoToAdd = new Photo(imageId);
        getImageById(photoToAdd);
    }
    private boolean reloadNeeded() {

        // If its the first album, load the album's photo
        if ((newAlbum != null) && (currentAlbum == null)) {
            currentAlbum = newAlbum;
            return true;
        }

        // If its not the first album, reload only if the album requested is not in memory
        if ((newAlbum != null) && (currentAlbum != null)) {
            if (newAlbum.getId() != currentAlbum.getId()) {
                currentAlbum = newAlbum;
                return true;
            }
        }

        // Its the same album - no need to reload
        return false;
    }

    public void setAlbum(int albumPosition) {
        newAlbum = AlbumsManager.getInstance().getAlbumByPosition(albumPosition);
        loadPhotos();
    }
}
