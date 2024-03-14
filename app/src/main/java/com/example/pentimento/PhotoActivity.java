package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PhotoActivity extends PhotoActivityMenusClass {

    private static final String TAG = PhotoActivity.class.getSimpleName();

    ImageView ivPhoto, secretIcon;
    TextView secretMsg;
    Boolean isSecretHidden;
    GalleryManager gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // Init photo area
        initPhotoArea();

    }

    protected int getLayoutId() {
        return R.layout.activity_photo;
    }

    private void initPhotoArea() {
        secretIcon = (ImageView) findViewById(R.id.ivHasSecretIcon);
        secretMsg = (TextView) findViewById(R.id.tvSecretMsg);
        isSecretHidden = true;

        secretIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSecretMessage();
            }
        });

        secretMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSecretMessage();
            }
        });

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSecretHidden) toggleSecretMessage();
            }
        });

    }

    private void toggleSecretMessage() {

        float startFade = 1f;
        float endFade = 0.4f;
        float startTextSize = 0f;
        float endTextSize = 24f;


        if (!isSecretHidden) {
            startFade = 0.4f;
            endFade = 1f;
            startTextSize = 24f;
            endTextSize = 0f;
        }

        isSecretHidden = !isSecretHidden;

        ValueAnimator photoAnimator = ValueAnimator.ofFloat(startFade, endFade);
        ValueAnimator secretAnimator = ValueAnimator.ofFloat(startTextSize, endTextSize);

        photoAnimator.setDuration(600);
        secretAnimator.setDuration(600);

        photoAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ivPhoto.setAlpha((float) animation.getAnimatedValue());
            }
        });


        secretAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                secretMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, animatedValue);
            }
        });

        photoAnimator.start();
        secretAnimator.start();

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