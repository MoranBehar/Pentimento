package com.example.pentimento;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.badge.BadgeUtils;

public class PhoneGalleryActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPhoneGallery;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_gallery);

        btnPhoneGallery = findViewById(R.id.btnPhoneGallery);
        btnPhoneGallery.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnPhoneGallery)
        {
            openPhoneGallery();
        }
    }

    private void openPhoneGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Do something with the selected image URI, such as displaying it in an ImageView
            Toast.makeText(this, "Image selected from gallery: " + imageUri.toString(), Toast.LENGTH_LONG).show();
        }
    }
}