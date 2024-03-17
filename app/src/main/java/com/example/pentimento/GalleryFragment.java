package com.example.pentimento;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


public class GalleryFragment extends Fragment {

    private GridView gvGallery;
    private PhotoAdapter adapter;

    public static String TAG = "Gallery";

    GalleryManager gm;

    private int albumNum;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        initGallery(view);

        return view;
    }

    private void initGallery(View view) {

        gm = GalleryManager.getInstance();
        gm.setErrorCallBack(this::errorHandler);

        adapter = new PhotoAdapter(getContext(), gm.getPhotosList(), R.layout.photo_item_grid);
        gm.setPhotoAdapter(adapter);
        gvGallery = view.findViewById(R.id.gvGallery);
        gvGallery.setAdapter(adapter);
        gvGallery.setOnItemClickListener(photoSelectedEvent());

        // album 0 is all photo, album 1 and up are the user's album
        albumNum = 0;
    }

    public void errorHandler(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, errorMessage);
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

        Intent intent = new Intent(this.getContext(), PhotoActivity.class);
        intent.putExtra("imagePosition", position);
        startActivity(intent);
    }


}