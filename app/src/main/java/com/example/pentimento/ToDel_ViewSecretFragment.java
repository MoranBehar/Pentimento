package com.example.pentimento;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class ToDel_ViewSecretFragment extends Fragment {

    private ListView lvViewSecretGallery;

    private FirebaseAuth fbAuth;

    private StorageReference storageRef;
    private FirebaseStorage fbStorage;

    private FirebaseFirestore fbDB;

    private List<Photo> gallery;

    private PhotoAdapter adapter;
    public static String TAG = "GalleryList";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_secret, container, false);

        findViews(view);

        initViewGalleryList();

        return view;
    }

    private void initViewGalleryList() {
        GalleryManager gm = GalleryManager.getInstance();
        gm.setErrorCallBack(this::errorHandler);

        adapter = new PhotoAdapter(getContext(), gm.getPhotosList(), R.layout.photo_item_list);
        gm.setPhotoAdapter(adapter);

        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseFirestore.getInstance();
        fbStorage = FirebaseStorage.getInstance();
        storageRef = fbStorage.getReference();

        lvViewSecretGallery.setAdapter(adapter);
        lvViewSecretGallery.setOnItemClickListener(photoSelectedEvent());
    }

    public void errorHandler(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, errorMessage);
    }

    private  AdapterView.OnItemClickListener photoSelectedEvent() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                comingSoonDialog();
            }
        };
    }


    private void findViews(View view) {
        lvViewSecretGallery = view.findViewById(R.id.lvViewSecretGallery);
    }

    private void comingSoonDialog() {
        AlertDialog.Builder confirmLogOut =
                new AlertDialog.Builder(getContext());

        confirmLogOut.setIcon(R.drawable.baseline_eye_24_black);
        confirmLogOut.setTitle("Coming Soon");
        confirmLogOut.setMessage("We are working on this option these days. " +
                "You will receive a notification when it is ready");
        confirmLogOut.setCancelable(false);

        confirmLogOut.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmLogOut.create().show();
    }
}