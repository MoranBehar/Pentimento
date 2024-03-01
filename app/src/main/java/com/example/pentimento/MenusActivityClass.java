package com.example.pentimento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

public abstract class MenusActivityClass extends AppCompatActivity {

    private DrawerLayout drawer;

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

    }

    private void showBottomSheetDialog() {

        View bottomSheetView = getLayoutInflater().inflate(R.layout.menu_bottom_sheet, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        ViewGroup viewGroup = (ViewGroup) bottomSheetView;
        setupBottomSheetDialogButtonsListener(viewGroup, bottomSheetDialog);

        bottomSheetDialog.show();
    }

    // Listen to click on any of the child elements of the view
    private void setupBottomSheetDialogButtonsListener(ViewGroup viewGroup, BottomSheetDialog bottomSheetDialog) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof Button) {
                child.setOnClickListener(v -> {
                    child.setOnClickListener(v1 -> {
                        if (v1.getId() == R.id.btn_option_1) {
                            Toast.makeText(viewGroup.getContext(), "1 Clicked", Toast.LENGTH_SHORT).show();
                        } else if (v1.getId() == R.id.btn_option_2) {
                            Toast.makeText(viewGroup.getContext(), "2 Clicked", Toast.LENGTH_SHORT).show();
                        }

                        bottomSheetDialog.dismiss();
                    });

                });
            }
        }
    }

    protected abstract int getLayoutId();


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
                new AlertDialog.Builder(MenusActivityClass.this);

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

}
