package com.example.pentimento;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;;
import java.util.UUID;

public class StorageManager {

    private static String TAG = "StorageManager";
    private static StorageManager instance;
    private FirebaseStorage storage;
    private StorageReference storageRef;



    private StorageManager() {
        initStorageManager();
    }

    public static StorageManager getInstance() {
        if (instance == null) {
            synchronized (StorageManager.class) {
                if(instance == null) {
                    instance = new StorageManager();
                }
            }
        }
        return instance;
    }

    private void initStorageManager() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public void uploadImageToStorage(Bitmap bitmap, StorageActionResult callback) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] arr = stream.toByteArray();
        UUID imageUUID = UUID.randomUUID();
        String imageId = imageUUID.toString();

        UploadTask task = storageRef.child("images/").child(imageId).putBytes(arr);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                DBManager.getInstance().connectImageToCurrentUser(imageId);
                callback.onSuccess(imageId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


}
