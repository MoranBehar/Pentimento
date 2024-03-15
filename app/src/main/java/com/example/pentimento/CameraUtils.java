package com.example.pentimento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

public class CameraUtils {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private StorageReference reference = storage.getReference();
    private FirebaseFirestore fbDB = FirebaseFirestore.getInstance();

    private static Activity myActivity;
    public CameraUtils(Activity activity)
    {
        this.myActivity = activity;
    }

    public void takePhoto() {
//        this.photoTakenListener = listener;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(myActivity.getPackageManager()) != null) {
            myActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(myActivity, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    public void addImageToFireBase(Bitmap newPhotoBitmap) {
//        Bitmap bitmap = ((BitmapDrawable)ivNewPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        newPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] arr = stream.toByteArray();
        UUID imageUUID = UUID.randomUUID();
        String imageId = imageUUID.toString();

        UploadTask task = reference.child("images/").child(imageId).putBytes(arr);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                connectUserToPhotos(imageId, fbAuth.getUid());
                Toast.makeText(myActivity, "Success = image uploaded"
                        ,Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(myActivity, "Failure = could not upload image"
                        ,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void connectUserToPhotos(String imageId, String userId) {
        Map<String, Object> image = new HashMap<>();
        image.put("id", imageId);
        image.put("Creator", userId);

        fbDB.collection("UserPhotos").document().set(image)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(myActivity,
                                "Success = image uploaded to your gallery"
                                ,Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(myActivity,
                                "Failure = could not upload image to your gallery"
                                ,Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createPhoto(ActivityResult result) {
        Bundle bundle = result.getData().getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
    }

}
