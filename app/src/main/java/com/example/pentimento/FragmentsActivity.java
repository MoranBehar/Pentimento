package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentsActivity extends MenusActivityClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        createFragment(new GalleryFragment());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragments;
    }

    private void createFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_gallery_fragment) {
                        selectedFragment = new GalleryFragment();
                    } else if (itemId == R.id.nav_add_secret_fragment) {
                        selectedFragment = new AddSecretFragment();
                    } else if (itemId == R.id.nav_view_fragment) {
                        selectedFragment = new ViewSecretFragment();
                    }


                    if (selectedFragment != null) {
                        createFragment(selectedFragment);
                    }

                    return true;
                }
            };


}