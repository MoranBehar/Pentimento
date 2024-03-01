package com.example.pentimento;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

        GalleryManager gm = GalleryManager.getInstance();
        gm.setErrorCallBack(this::errorHandler);

        adapter = new PhotoAdapter(getContext(), gm.getGalleryList(), R.layout.photo_item_grid);
        gm.setPhotoAdapter(adapter);
        gvGallery = view.findViewById(R.id.gvGallery);
        gvGallery.setAdapter(adapter);
        gvGallery.setOnItemClickListener(photoSelectedEvent());

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
        startActivity(intent);

//        // Create a photo fragment
//        Fragment newFragment = new PhotoFragment();
//
//        // Get the fragments manager of my parent activity (FragmentsActivity) and start a transaction
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//        // Perform a switch - replace current fragment with the photo fragment
//        // also keep the current fragment to the back stack to support the back button
//        transaction.replace(R.id.fragment_container, newFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
    }


}