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

public class SharedPhotosManager extends BasePhotoManager {

    private static String TAG = "SharedPhotosManager";
    private static SharedPhotosManager instance;
    private static ArrayList<SharedPhoto> photoArrayList;

    // Implement Singleton
    private SharedPhotosManager() {
        super();
    }

    public static SharedPhotosManager getInstance() {
        if (instance == null) {
            synchronized (GalleryManager.class) {
                if (instance == null) {
                    instance = new SharedPhotosManager();
                }
            }
        }
        return instance;
    }

    protected void loadPhotos() {

        CollectionReference colRef = fbDB.collection("PhotoSharing");
        colRef.whereEqualTo("sharedTo", fbAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> row = document.getData();

                                createItem(
                                        row.get("photoId").toString(),
                                        row.get("sharedBy").toString(),
                                        row.get("sharedOn").toString());
                            }

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

    protected void createItem(String imageId, String sharedById, String sharedOn) {

        DBManager.getInstance().getUserById(sharedById, new DBActionResult<User>() {
            @Override
            public void onSuccess(User sharedBy) {
                Photo photoToAdd = new SharedPhoto(imageId, sharedBy, sharedOn);
                getImageById(photoToAdd);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

}
