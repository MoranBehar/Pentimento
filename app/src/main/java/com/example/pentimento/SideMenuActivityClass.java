package com.example.pentimento;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SideMenuActivityClass extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
                new AlertDialog.Builder(SideMenuActivityClass.this);

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
