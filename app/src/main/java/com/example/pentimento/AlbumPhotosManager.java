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

    private String albumId;

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
        // TODO - Get the photos of a specific album
    }
    protected void createItem(String imageId) {}


}
