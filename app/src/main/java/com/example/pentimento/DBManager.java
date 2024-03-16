package com.example.pentimento;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    public void uploadImageToStorage(Bitmap bitmap, iDBActionResult callback) {

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
                callback.onSuccess(imageId);
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

    public void sharePhotoToUser(String photoId, String toUserId) {

        Map<String, Object> shareObject = new HashMap<>();
        shareObject.put("photoId", photoId);
        shareObject.put("sharedBy", fbAuth.getUid());
        shareObject.put("sharedTo", toUserId);
        shareObject.put("sharedOn", getCurrentDate());


        fbDB.collection("PhotoSharing").document().set(shareObject)
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

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void createAlbum(String albumName, String userId) {

        Album newAlbum = new Album(userId, albumName);
        fbDB.collection("Albums").document().set(newAlbum)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void addPhotoToAlbum(String albumId, String photoId) {

        AlbumPhotos albumPhotos = new AlbumPhotos(albumId, photoId);

        fbDB.collection("AlbumPhotos").document().set(albumPhotos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    public void getUserAlbums() {
        String uid = fbAuth.getUid();

        CollectionReference colRef = fbDB.collection("Albums");
        colRef.whereEqualTo("ownerId", uid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // Create a list of Album objects
                        List<Album> albumList = new ArrayList<>();

                        //get the albums
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> row = document.getData();
                            Log.d(TAG, "onComplete: ");

                            String ownerId = row.get("ownerId").toString();
                            String title = row.get("title").toString();

                            Album newAlbum = new Album(ownerId, title);
                            albumList.add(newAlbum);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
