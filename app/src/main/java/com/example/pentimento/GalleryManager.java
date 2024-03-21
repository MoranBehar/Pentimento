package com.example.pentimento;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class GalleryManager extends BasePhotoManager {

    private static String TAG = "GalleryManager";


    // Implement as Singleton
    private static GalleryManager instance;

    private GalleryManager() {
        super();
    }

    public static GalleryManager getInstance() {
        if (instance == null) {
            synchronized (GalleryManager.class) {
                if (instance == null) {
                    instance = new GalleryManager();
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
                                Photo photo = document.toObject(Photo.class);
                                getImageById(photo);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

}
