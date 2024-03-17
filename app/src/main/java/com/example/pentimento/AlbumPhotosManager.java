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
        CollectionReference colRef = fbDB.collection("UserPhotos");
        colRef.whereEqualTo("Creator", fbAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> row = document.getData();
                                createItem(row.get("id").toString());
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

    protected void createItem(String imageId) {
        Photo photoToAdd = new Photo(imageId);
        getImageById(photoToAdd);
    }


}
