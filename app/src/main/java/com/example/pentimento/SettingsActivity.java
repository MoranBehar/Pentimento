package com.example.pentimento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViews();
        setListeners();
    }

    private void setListeners() {
        btnClose.setOnClickListener(this);
    }

    private void findViews() {
        btnClose=findViewById(R.id.btnClose);
    }

    @Override
    public void onClick(View v) {
        if(v == btnClose)
        {
            finish();
        }
    }
}