package com.example.pentimento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public abstract class MenusActivityClass extends AppCompatActivity {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer_menu);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation view item clicks here.
        NavigationView sideNavDrawerView = findViewById(R.id.nav_view);
        setupDrawerListener(sideNavDrawerView);

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
        int id=item.getItemId();

        if(id==R.id.itemLogout){
            checkBeforeLoggingOut();
        }
        else if(id==R.id.itemUserInfo)
        {
            Intent intent=new Intent(this, UserInfoActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.itemSettings)
        {
            Intent intent=new Intent(this, SettingsActivity.class);
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
