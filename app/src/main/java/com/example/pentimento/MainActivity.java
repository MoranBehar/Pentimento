package com.example.pentimento;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    ImageView ivPhoto;
    TextView tvMsg;
    Button btnAddMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViews();
        setListeners();
    }

    private void findViews() {
        ivPhoto = findViewById(R.id.ivPhoto);
        tvMsg = findViewById(R.id.tvMsg);
        btnAddMsg = findViewById(R.id.btnAddMsg);
    }

    private void setListeners() {
        btnAddMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnAddMsg)
        {
            addSecretMsgToPhoto();
            grayScale();
        }
    }

    private void addSecretMsgToPhoto() {
        EmbedMessage MsgEmbed = new EmbedMessage("My Message");
        MsgEmbed.execute();
    }

    private void grayScale() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivPhoto.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ImageProcessor IP = new ImageProcessor();
        Bitmap newBitmap = IP.grayScale(bitmap, 70);

        ivPhoto.setImageBitmap(newBitmap);

    }
}