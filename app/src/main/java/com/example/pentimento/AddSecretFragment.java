package com.example.pentimento;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class AddSecretFragment extends Fragment
        implements View.OnClickListener, GalleryFragment.DialogListener {

    private Button btnChoosePhoto, btnAddSecret;

    private ImageView ivAddSecretPhoto;

    private FirebaseAuth fbAuth;

    private StorageReference storageRef;
    private FirebaseStorage fbStorage;

    private FirebaseFirestore fbDB;

    private List<Photo> gallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_secret, container, false);

        findViews(view);
        setListeners();

        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseFirestore.getInstance();
        fbStorage = FirebaseStorage.getInstance();
        storageRef = fbStorage.getReference();

        gallery = new ArrayList<>();

        return view;
    }

    private void findViews(View view) {
        btnChoosePhoto = view.findViewById(R.id.btnChoosePhoto);
        btnAddSecret = view.findViewById(R.id.btnAddSecret);
        ivAddSecretPhoto = view.findViewById(R.id.ivAddSecretPhoto);
    }

    private void setListeners() {
        btnChoosePhoto.setOnClickListener(this);
        btnAddSecret.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnChoosePhoto)
        {
            showGallery();
        }
        else if(v == btnAddSecret)
        {
            comingSoonDialog();
        }
    }

    private void comingSoonDialog() {
        AlertDialog.Builder confirmLogOut =
                new AlertDialog.Builder(getContext());

        confirmLogOut.setIcon(R.drawable.baseline_lock_24);
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

    private void showGallery() {
        GalleryFragment myGallery = new GalleryFragment();
        myGallery.setListener(this);
        myGallery.show(getChildFragmentManager(), GalleryFragment.TAG);
    }

    public void onPhotoSelected(Photo imageSelected) {
        ivAddSecretPhoto.setImageBitmap(imageSelected.getPhoto());
        btnAddSecret.setEnabled(true); //be able to add secret to photo
    }

}