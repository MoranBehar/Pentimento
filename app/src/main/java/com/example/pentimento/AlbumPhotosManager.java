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
    private ArrayList<Photo> photos;
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
            CollectionReference colRef = fbDB.collection("AlbumPhotos");
            colRef.whereEqualTo("albumId", newAlbum.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Photo photoToAdd = document.toObject(Photo.class);
                                    photos.add(photoToAdd);
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }

    }

    private boolean reloadNeeded() {
        if (newAlbum != null) {
            return true;
        }

        return false;
    }

    public void setAlbum(int albumPosition) {
        newAlbum = AlbumsManager.getInstance().getAlbumByPosition(albumPosition);
        loadPhotos();
    }
}
