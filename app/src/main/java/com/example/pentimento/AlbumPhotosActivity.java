package com.example.pentimento;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class AlbumPhotosActivity extends AlbumPhotosActivityMenusClass
        implements View.OnClickListener, editAlbumNameDialogFragment.DialogListener {

    public static String TAG = "AlbumPhotosFragment";

    private GridView gvGallery;
    private PhotoAdapter adapter;

    private Album album;
    private int albumPosition;
    private AlbumPhotosManager apManager;

    private DBManager dbManager;

    ImageButton btn_albumPhotosToolbar_edit;

    TextView tvAlbumTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the image resource position from the intent
        albumPosition = getIntent().getIntExtra("albumPosition", -1);

        initGallery();
        setEditAlbumNameBtn();
        tvAlbumTitle = findViewById(R.id.tvAlbumTitle);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_photos;
    }

    private void initGallery() {

        apManager = AlbumPhotosManager.getInstance();
        apManager.setErrorCallBack(this::errorHandler);
        adapter = new PhotoAdapter(this, apManager.getPhotosList(), R.layout.photo_item_grid);
        apManager.setPhotoAdapter(adapter);
        gvGallery = findViewById(R.id.gvAlbumPhotosGallery);
        gvGallery.setAdapter(adapter);
        apManager.setAlbum(albumPosition);

        album = AlbumsManager.getInstance().getAlbumByPosition(albumPosition);

        setPageTitle();

        TextView createDate = findViewById(R.id.tvAlbumCreateDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        createDate.setText(sdf.format(album.getCreateDate()));

        gvGallery.setOnItemClickListener(photoSelectedEvent());

        dbManager = DBManager.getInstance();

        // Log view
        DBManager.getInstance().addLogEntry(album.getId(), 2);
    }

    private void setPageTitle() {
        String formattedTitle = String.format("%s Album (%d)", album.getTitle(), album.getNumOfPhotos());
        TextView title = findViewById(R.id.tvAlbumTitle);
        title.setText(formattedTitle);
    }

    private void setEditAlbumNameBtn() {
        btn_albumPhotosToolbar_edit = findViewById(R.id.btn_albumPhotosToolbar_edit);
        btn_albumPhotosToolbar_edit.setOnClickListener(this);
    }

    private  AdapterView.OnItemClickListener photoSelectedEvent() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                photoClicked(position);
            }
        };
    }

    private void photoClicked(int position) {
        Photo photoToAdd = apManager.getPhotoByPosition(position);

        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("photoId", photoToAdd.getId());
        startActivity(intent);
    }

    public void errorHandler(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, errorMessage);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_albumPhotosToolbar_edit){
            openEditAlbumNameDialogFragment();
        }
    }

    private void openEditAlbumNameDialogFragment() {
        editAlbumNameDialogFragment dialogFragment = new editAlbumNameDialogFragment();

        FragmentActivity fragmentActivity = (FragmentActivity) AlbumPhotosActivity.this;

        // Set the listener
        dialogFragment.setDialogListener(this::onDialogDataReturn);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "YourDialogFragment");
    }

    @Override
    public void onDialogDataReturn(String photoName) {
        album.setTitle(photoName);

        dbManager.updateAlbumTitle(album);

        //set the ui text to the updated name
        setPageTitle();
    }

    public void onResume() {
        super.onResume();
        apManager.loadPhotos();
        adapter.notifyDataSetChanged();
    }
}