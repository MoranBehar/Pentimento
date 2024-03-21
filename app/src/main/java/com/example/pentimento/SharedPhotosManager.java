package com.example.pentimento;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class SharedPhotosManager extends BasePhotoManager {

    private static String TAG = "SharedPhotosManager";
    private static SharedPhotosManager instance;

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

        // Register to get future realtime updates on new sharing
        DBManager.getInstance().photoSharingRealTimeUpdates(new DBManager.DBActionResult<QueryDocumentSnapshot>() {

            @Override
            public void onSuccess(QueryDocumentSnapshot document) {
                Map<String, Object> row = document.getData();

                createItem(
                        row.get("photoId").toString(),
                        row.get("sharedBy").toString(),
                        row.get("sharedOn").toString());
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    protected void createItem(String imageId, String sharedById, String sharedOn) {

        DBManager.getInstance().getUserById(sharedById, new DBManager.DBActionResult<User>() {
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
