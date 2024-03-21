package com.example.pentimento;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

//        newLoadPhotos();
        newLoadPhotos2();

//        CollectionReference colRef = fbDB.collection("UserPhotos");
//        colRef.whereEqualTo("ownerId", fbAuth.getUid())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Photo photo = document.toObject(Photo.class);
//                                getImageById(photo);
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                        }
//                    }
//                });

    }


    protected void newLoadPhotos2() {
        CollectionReference colRef = fbDB.collection("UserPhotos");

        colRef.whereEqualTo("ownerId", fbAuth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listener failed.", e);
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "New photo shared: " + dc.getDocument().getData());
                                    Photo photo = dc.getDocument().toObject(Photo.class);
                                    getImageById(photo);
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Share Removed: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });
    }
//        protected void newLoadPhotos () {
//
//
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            Handler handler = new Handler(Looper.getMainLooper());
//
//            executor.execute(() -> {
//                CollectionReference colRef = fbDB.collection("UserPhotos");
//                colRef.whereEqualTo("ownerId", fbAuth.getUid())
//                        .get().addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Photo photo = document.toObject(Photo.class);
//                                        getImageById(photo);
//                                    }
//                                } else {
//                                    Log.d(TAG, "get failed with ", task.getException());
//                                }
//                            } else {
//                                Log.d("Executor", "Error getting documents: ", task.getException());
//                            }
//                        });
//
//
//            });
//        }

    }
