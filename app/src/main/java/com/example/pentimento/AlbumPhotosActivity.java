package com.example.pentimento;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class AlbumPhotosActivity extends Activity {

    public static String TAG = "AlbumPhotosFragment";

    private GridView gvGallery;
    private PhotoAdapter adapter;

    private Album album;
    private int albumPosition;
    private AlbumPhotosManager apManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_photos);

        // Get the image resource position from the intent
        albumPosition = getIntent().getIntExtra("albumPosition", -1);

        initGallery();


    }

    private void initGallery() {

        apManager = AlbumPhotosManager.getInstance();
        apManager.setErrorCallBack(this::errorHandler);
        adapter = new PhotoAdapter(this, apManager.getPhotosList(), R.layout.photo_item_grid);
        apManager.setPhotoAdapter(adapter);
        gvGallery = findViewById(R.id.gvAlbumPhotosGallery);
        gvGallery.setAdapter(adapter);
        apManager.setAlbum(albumPosition);

        TextView title = findViewById(R.id.tvAlbumTitle);
        album = AlbumsManager.getInstance().getAlbumByPosition(albumPosition);
        title.setText(album.getTitle());

//        gvGallery.setOnItemClickListener(photoSelectedEvent());

    }

    public void errorHandler(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, errorMessage);
    }
}