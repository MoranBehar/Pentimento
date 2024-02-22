package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class GalleryManager {

    private FirebaseAuth fbAuth;
    private StorageReference storageRef;
    private FirebaseStorage fbStorage;
    private FirebaseFirestore fbDB;


    private ArrayList<Photo> gallery;
    private PhotoAdapter viewAdapter;

    private Consumer<String> errorCallBack;

    private static String TAG = "GalleryManager";

    public GalleryManager() {
        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseFirestore.getInstance();
        fbStorage = FirebaseStorage.getInstance();
        storageRef = fbStorage.getReference();

        gallery = new ArrayList<Photo>();

        getUserGallery();
    }

    public void setErrorCallBack(Consumer<String> callBack) {
        errorCallBack = callBack;
    }

    public ArrayList<Photo> getGalleryList() {
        return gallery;
    }

    public void setPhotoAdapter(PhotoAdapter adapter) {
        viewAdapter = adapter;
    }

    private void getUserGallery() {
        CollectionReference colRef = fbDB.collection("UserPhotos");
        colRef.whereEqualTo("Creator", fbAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> row = document.getData();
                                getImageById(row.get("id").toString());
                            }

                        }
                        else
                        {
                            Log.d(TAG, "get failed with " , task.getException());
                        }
                    }
                });

    }

    private void getImageById(String imageId) {
        StorageReference photoRef = storageRef.child("images/" +imageId);
        photoRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                        addToGallery(bitmap);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        notifyError("Could not load image");
                    }
                });
    }

    private void addToGallery(Bitmap newImageBitMap) {
        Photo photoToAdd = new Photo(newImageBitMap);
        gallery.add(photoToAdd);
        viewAdapter.notifyDataSetChanged();
    }

    private void notifyError(String errorMessage) {

        if (errorCallBack != null) {
            errorCallBack.accept(errorMessage);
        }
    }
}
