package com.example.pentimento;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

public abstract class HomeActivityMenusClass extends AppCompatActivity {

    private DrawerLayout drawer;
    private BottomSheetDialog bottomSheetDialog;

    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        // Set the top appbar to our toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the drawer manager responsible for opening/closing the side menu
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initBottomSheetDialog();

        // Set add button to open bottom menu
        ImageButton addButton = findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });

        // Handle side menu navigation view item clicks
        NavigationView sideNavDrawerView = findViewById(R.id.nav_side_menu_view);
        setupDrawerListener(sideNavDrawerView);

        dbManager = DBManager.getInstance();
    }

    private void initBottomSheetDialog() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.menu_bottom_sheet, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        ViewGroup viewGroup = (ViewGroup) bottomSheetView;
        setupBottomSheetDialogButtonsListener(viewGroup, bottomSheetDialog);
    }

    private void showBottomSheetDialog() {
        bottomSheetDialog.show();
    }

    // Listen to click on any of the child elements of the view
    private void setupBottomSheetDialogButtonsListener(ViewGroup viewGroup, BottomSheetDialog bottomSheetDialog) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);

            // Only set listeners to Buttons
            if (child instanceof Button) {
                child.setOnClickListener(v -> {
                    if (v.getId() == R.id.btn_take_picture) {
                        TakePhotoToApp();
                    }
                    else if (v.getId() == R.id.btn_from_phone_gallery) {
                        getPhoneGallery();
                    }

                    // Dismiss the BottomSheetDialog
                    bottomSheetDialog.dismiss();
                });
            }
        }
    }

    protected abstract int getLayoutId();

     private void getPhoneGallery(){
        Intent intent = new Intent(getBaseContext(), PhoneGalleryActivity.class);
        startActivity(intent);
    }


    private void setupDrawerListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {

                    // Handle the item clicked
                    onDrawerItemSelected(menuItem);

                    // Close drawer
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
        );
    }

    private boolean onDrawerItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemLogout) {
            checkBeforeLoggingOut();
        } else if (id == R.id.itemUserInfo) {
            Intent intent = new Intent(this, UserInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.itemSettings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkBeforeLoggingOut() {
        AlertDialog.Builder confirmLogOut =
                new AlertDialog.Builder(HomeActivityMenusClass.this);

        confirmLogOut.setIcon(R.drawable.baseline_logout_24);
        confirmLogOut.setTitle("Confirm Logging Out");
        confirmLogOut.setMessage("Are you sure you want to log out?");
        confirmLogOut.setCancelable(false);

        confirmLogOut.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backToStart();
            }
        });

        confirmLogOut.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmLogOut.create().show();
    }

    private void backToStart() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void TakePhotoToApp(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhoto.launch(intent);
    }


    private ActivityResultLauncher takePhoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        dbManager.saveImageToDB(bitmap);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Could not take photo", Toast.LENGTH_LONG).show();
                    }
                }
            });
}
