package com.example.pentimento;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class PhotoActivityMenusClass extends AppCompatActivity {

    private static final String TAG = PhotoActivityMenusClass.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        // Set the top appbar to our toolbar
        Toolbar toolbar = findViewById(R.id.photoToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        setToolbarListeners();

    }

    protected abstract int getLayoutId();

    private void setToolbarListeners() {
        findViewById(R.id.btn_photoToolbar_back).setOnClickListener(this::onToolbarItemClick);
        findViewById(R.id.btn_photoToolbar_add).setOnClickListener(this::onToolbarItemClick);
        findViewById(R.id.btn_photoToolbar_edit).setOnClickListener(this::onToolbarItemClick);
    }

    private void onToolbarItemClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_photoToolbar_back) {
            backBtnClicked();
        } else if (id == R.id.btn_photoToolbar_add) {
            addBtnClicked();
        } else if (id == R.id.btn_photoToolbar_edit) {
            editBtnClicked();
        }
    }

    private void backBtnClicked() {
        finish();
    }

    private void addBtnClicked() {
        Toast.makeText(this, "add clicked", Toast.LENGTH_SHORT).show();
    }

    private void editBtnClicked() {
        Toast.makeText(this, "edit clicked", Toast.LENGTH_SHORT).show();
    }

}
