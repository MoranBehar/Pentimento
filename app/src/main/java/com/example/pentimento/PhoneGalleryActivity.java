package com.example.pentimento;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.badge.BadgeUtils;

public class PhoneGalleryActivity extends AppCompatActivity  {

    Button btnPhoneGallery;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_gallery);


        openPhoneGallery();
//
//        btnPhoneGallery = findViewById(R.id.btnPhoneGallery);
//        btnPhoneGallery.setOnClickListener(this);

    }

//    @Override
//    public void onClick(View v) {
//        if(v == btnPhoneGallery)
//        {
//            openPhoneGallery();
//        }
//    }

    private void openPhoneGallery() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);


        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
//            // Do something with the selected image URI, such as displaying it in an ImageView
//            Toast.makeText(this, "Image selected from gallery: " + imageUri.toString(), Toast.LENGTH_LONG).show();
//        }
//    }
}