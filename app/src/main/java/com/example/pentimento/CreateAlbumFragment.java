package com.example.pentimento;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class CreateAlbumFragment extends DialogFragment {

    EditText etAlbumName;

    private DBManager dbManager;
    FirebaseAuth fbAuth;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the dialog layout for this fragment
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_create_album, null);
        builder.setView(dialogView);

        etAlbumName = dialogView.findViewById(R.id.etAlbumName);

        dbManager = DBManager.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        builder.setPositiveButton("Create", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Album newAlbum = new Album(fbAuth.getUid(), etAlbumName.getText().toString());
                        dbManager.createAlbum(newAlbum, new DBManager.DBActionResult<String>() {
                            @Override
                            public void onSuccess(String data) {
                                UIAlerts.InfoAlert("Create Album",
                                        "New Album created",
                                        dialogView.getContext());
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                });

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =  inflater.inflate(R.layout.fragment_create_album, container, false);

        return view;
    }
}