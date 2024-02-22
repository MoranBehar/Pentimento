package com.example.pentimento;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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


public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btnTakePhoto, btnUploadToGallery;

    private ImageView ivNewPhoto;

    private FirebaseAuth fbAuth;

    private StorageReference reference;

    private FirebaseFirestore fbDB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findViews(view);
        setListeners();

        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseFirestore.getInstance();

        return view;
    }

    private void findViews(View view) {
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto);
        btnUploadToGallery = view.findViewById(R.id.btnUploadToGallery);
        ivNewPhoto = view.findViewById(R.id.ivNewPhoto);
    }

    private void setListeners() {
        btnTakePhoto.setOnClickListener(this);
        btnUploadToGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnTakePhoto)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhoto.launch(intent);
        }
        else if(v == btnUploadToGallery)
        {
            //adding photo to the firebase
            FirebaseStorage storage = FirebaseStorage.getInstance();
            reference = storage.getReference();

            addImageToFireBase();
        }
    }

    private ActivityResultLauncher takePhoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        createPhoto(result);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Could not take photo",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

    private void addImageToFireBase() {
        Bitmap bitmap = ((BitmapDrawable)ivNewPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] arr = stream.toByteArray();
        UUID imageUUID = UUID.randomUUID();
        String imageId = imageUUID.toString();

        UploadTask task = reference.child("images/").child(imageId).putBytes(arr);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                connectUserToPhotos(imageId, fbAuth.getUid());
                Toast.makeText(getContext(), "Success = image uploaded"
                        ,Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failure = could not upload image"
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
                        Toast.makeText(getContext(),
                                "Success = image uploaded to your gallery"
                                ,Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),
                                "Failure = could not upload image to your gallery"
                                ,Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createPhoto(ActivityResult result) {
        Bundle bundle = result.getData().getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        ivNewPhoto.setImageBitmap(bitmap);
        btnUploadToGallery.setEnabled(true); //be able to upload the photo
    }
}