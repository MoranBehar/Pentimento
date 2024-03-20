package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;;
import java.io.File;
import java.nio.ByteBuffer;
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

    public void uploadImageToStorage(String imageId, byte[] imageBytes, StorageActionResult callback) {

        UploadTask task = storageRef.child("images/").child(imageId).putBytes(imageBytes);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callback.onSuccess(imageId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void getImageById(String imageId,  StorageActionResult callback) {
        StorageReference photoRef = storageRef.child("images/" +imageId);
        photoRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        callback.onSuccess(bytes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e);
                    }
                });
    }

    public void addImageToStorage(Bitmap bitmap, StorageActionResult callback) {
        UUID imageUUID = UUID.randomUUID();
        String imageId = imageUUID.toString();

        byte[] arr = convertBitmapToPNG(bitmap);
        uploadImageToStorage(imageId, arr, callback);

    }
    public void updateImageInStorage(Photo photoToUpdate, StorageActionResult callback) {
        byte[] arr = convertBitmapToPNG(photoToUpdate.getPhoto());
        uploadImageToStorage(photoToUpdate.getId(), arr, callback);
    }

    private byte[] convertBitmapToPNG(Bitmap bp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] arr = stream.toByteArray();
        return arr;
    }

}
