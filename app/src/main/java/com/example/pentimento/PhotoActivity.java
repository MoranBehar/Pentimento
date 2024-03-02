package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PhotoActivity extends AppCompatActivity {

    ImageView ivPhoto;
    GalleryManager gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        ivPhoto = findViewById(R.id.ivPhoto);
        gm = GalleryManager.getInstance();


        // Get the image resource position from the intent
        int imageSrcPosition = getIntent().getIntExtra("imagePosition", -1);
        if (imageSrcPosition != -1) {
            //get the image src
            Photo image = gm.getImageByPosition(imageSrcPosition);

            // Set the image resource to the ImageView
            ivPhoto.setImageBitmap(image.getPhoto());
        }

    }


    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_share_fragment) {
                        sharePhoto();
                    }
                    else if (itemId == R.id.nav_secret_fragment) {
                        secretManger();
                    }
                    else if (itemId == R.id.nav_favorite_fragment) {
                        favToggle();
                    }
                    else if (itemId == R.id.nav_delete_fragment) {
                        deletePhoto();
                    }

                    return true;
                }
            };

    private void deletePhoto() {
        checkBeforeDeleting();
    }

    private void favToggle() {
        Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();

    }

    private void secretManger() {
        Toast.makeText(this, "secret", Toast.LENGTH_SHORT).show();

    }

    private void sharePhoto() {
        Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();

    }


    private void checkBeforeDeleting() {
        AlertDialog.Builder confirmDeletingPhoto = new AlertDialog.Builder(this);

        confirmDeletingPhoto.setIcon(R.drawable.baseline_delete_24);
        confirmDeletingPhoto.setTitle("Delete this photo");
        confirmDeletingPhoto.setMessage("Are you sure you want to delete this photo from you gallery?");
        confirmDeletingPhoto.setCancelable(false);

        confirmDeletingPhoto.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startDeletingProcess();
            }
        });

        confirmDeletingPhoto.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmDeletingPhoto.create().show();
    }

    private void startDeletingProcess() {
    }
}