package com.example.pentimento;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class editAlbumNameDialogFragment extends DialogFragment {

    EditText etAlbumName;

    public interface DialogListener {
        void onDialogDataReturn(String albumName);
    }
    private editAlbumNameDialogFragment.DialogListener listener;

    // Method to attach the listener
    public void setDialogListener(editAlbumNameDialogFragment.DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the dialog layout for this fragment
        View dialogView = getActivity().getLayoutInflater().
                inflate(R.layout.fragment_edit_album_name_dialog, null);
        builder.setView(dialogView);

        etAlbumName = dialogView.findViewById(R.id.etAlbumName);

        builder.setPositiveButton("Save", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogDataReturn(etAlbumName.getText().toString());
                    }
                });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        View view = inflater.inflate(R.layout.fragment_edit_album_name_dialog, container, false);

        return view;
    }
}