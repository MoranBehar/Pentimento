package com.example.pentimento;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivPhoto;
    TextView tvMsg;
    Button btnAddMsg, btnGetMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViews();
        setListeners();
    }

    private void findViews() {
        ivPhoto = findViewById(R.id.ivPhoto);
        tvMsg = findViewById(R.id.tvMsg);
        btnAddMsg = findViewById(R.id.btnAddMsg);
        btnGetMsg = findViewById(R.id.btnGetMsg);
    }

    private void setListeners() {
        btnAddMsg.setOnClickListener(this);
        btnGetMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnAddMsg)
        {
            addSecretMsgToPhoto(createBitmapToImage());
//            grayScale();
        }
        else if(v == btnGetMsg)
        {
            getSecretMsgFromPhoto(createBitmapToImage());
        }
    }

    private Bitmap createBitmapToImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivPhoto.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        return  bitmap;
    }

    private void addSecretMsgToPhoto(Bitmap image) {
        ImageLsbManipulation MsgEmbed = new ImageLsbManipulation("a", image);
        Bitmap bitmapWithMsg = MsgEmbed.EmbedMessageAction();
        ivPhoto.setImageBitmap(bitmapWithMsg);
    }

    private void getSecretMsgFromPhoto(Bitmap image) {
        ImageLsbManipulation extractMessage = new ImageLsbManipulation(image);
        extractMessage.getMassage();
    }

    private void grayScale() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivPhoto.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ImageProcessor IP = new ImageProcessor();
        Bitmap newBitmap = IP.grayScale(bitmap, 70);

        ivPhoto.setImageBitmap(newBitmap);

    }
}