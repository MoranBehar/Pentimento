package com.example.pentimento;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
                if (instance == null) {
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

    public void getImageById(String imageId, StorageActionResult callback) {
        StorageReference photoRef = storageRef.child("images/" + imageId);
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

    public void downloadPhotoAsPNG(Context context, Photo photoToDownload) {

        // Save the photo to the device's pictures directory
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, "Pentimento_" + photoToDownload.getId() + ".png");

        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            // Compress the bitmap as PNG and save it to the file
            photoToDownload.getPhoto().compress(Bitmap.CompressFormat.PNG, 100, out);
            UIAlerts.InfoAlert("Save Photo", "Photo saved to pictures directory",context);
            Log.d(TAG, "Photo saved to pictures directory");
        } catch (IOException e) {
            Log.d(TAG, "Failed saving photo to pictures directory - file not found");
        }
    }

    public void deletePhoto(String photoId, StorageActionResult callback) {
        // create a reference to the file to delete
        StorageReference fileRef = storageRef.child("images/" + photoId);

        // Delete the file
        fileRef.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String Delete = "Photo deleted from your storage";
                        callback.onSuccess(Delete);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MyActivity", "Error deleting file: " + e.getMessage());
                    }
                });
    }

}
