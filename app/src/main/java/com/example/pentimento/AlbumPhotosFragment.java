package com.example.pentimento;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class AlbumPhotosFragment extends Fragment {

    public static String TAG = "AlbumPhotosFragment";

    private GridView gvGallery;
    private PhotoAdapter adapter;

    private int albumPosition;
    private AlbumPhotosManager apManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String data = getArguments().getString("albumPosition");
            albumPosition = Integer.parseInt(data);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album_photos, container, false);

        initGallery(view);

        return view;
    }

    private void initGallery(View view) {

        apManager = AlbumPhotosManager.getInstance();
        apManager.setErrorCallBack(this::errorHandler);
        apManager.setAlbum(albumPosition);

        adapter = new PhotoAdapter(getContext(), apManager.getPhotosList(), R.layout.photo_item_grid);
        apManager.setPhotoAdapter(adapter);
        gvGallery = view.findViewById(R.id.gvAlbumPhotosGallery);
        gvGallery.setAdapter(adapter);

//        gvGallery.setOnItemClickListener(photoSelectedEvent());

        TextView tvAlbumId = view.findViewById(R.id.tvAlbumId);
        tvAlbumId.setText(String.valueOf(albumPosition));
    }

    public void errorHandler(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, errorMessage);
    }
}