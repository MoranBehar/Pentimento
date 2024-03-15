package com.example.pentimento;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DBManager {

    private static String TAG = "DBManager";
    private static DBManager instance;

    private FirebaseAuth fbAuth;
    private FirebaseFirestore fbDB;
    private FirebaseStorage storage;
    private StorageReference storageRef;



    private DBManager() {
        initDBManager();
    }

    public static DBManager getInstance() {
        if (instance == null) {
            synchronized (DBManager.class) {
                if(instance == null) {
                    instance = new DBManager();
                }
            }
        }
        return instance;
    }

    private void initDBManager() {
        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }


    public void uploadImageToStorage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] arr = stream.toByteArray();
        UUID imageUUID = UUID.randomUUID();
        String imageId = imageUUID.toString();

        UploadTask task = storageRef.child("images/").child(imageId).putBytes(arr);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                connectImageToUser(imageId, fbAuth.getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void connectImageToUser(String imageId, String userId) {
        Map<String, Object> image = new HashMap<>();
        image.put("id", imageId);
        image.put("Creator", userId);

        fbDB.collection("UserPhotos").document().set(image)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}
