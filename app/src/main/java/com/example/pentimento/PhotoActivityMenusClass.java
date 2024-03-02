package com.example.pentimento;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class PhotoActivityMenusClass extends AppCompatActivity {

    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        // Set the top appbar to our toolbar
        Toolbar toolbar = findViewById(R.id.photoToolbar);
        setSupportActionBar(toolbar);

        backToGallery();
    }

    protected abstract int getLayoutId();


    //TODO - doesn't work
    private void backToGallery(){
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
