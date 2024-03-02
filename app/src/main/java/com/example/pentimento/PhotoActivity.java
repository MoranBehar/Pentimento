package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
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
        Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
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
}