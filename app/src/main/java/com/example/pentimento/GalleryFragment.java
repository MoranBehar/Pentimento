package com.example.pentimento;

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

    public interface PhotoListener {
        void onPhotoSelected(Photo selectedImage);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        initGallery(view);

        return view;
    }

    private void initGallery(View view) {
        GalleryManager gm = new GalleryManager();
        gm.setErrorCallBack(this::errorHandler);

        adapter = new PhotoAdapter(getContext(), gm.getGalleryList(), R.layout.photo_item_grid);
        gm.setPhotoAdapter(adapter);
        gvGallery = view.findViewById(R.id.gvGallery);
        gvGallery.setAdapter(adapter);
        gvGallery.setOnItemClickListener(photoSelectedEvent());

        //TODO - do i need the frid view adapter or the photo adapter enough
//        GridViewAdapter adapter = new GridViewAdapter(this.getContext());
//        gvGallery.setAdapter(adapter);
    }

    public void errorHandler(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, errorMessage);
    }

    private  AdapterView.OnItemClickListener photoSelectedEvent() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                listener.onPhotoSelected((Photo)parent.getItemAtPosition(position));
//                dismiss();
            }
        };
    }


}