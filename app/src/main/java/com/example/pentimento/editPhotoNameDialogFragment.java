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

public class editPhotoNameDialogFragment extends DialogFragment {

    EditText etPhotoName;

    public interface DialogListener {
        void onDialogDataReturn(String secretMsg);
    }
    private DialogListener listener;

    // Method to attach the listener
    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the dialog layout for this fragment
        View dialogView = getActivity().getLayoutInflater().
                inflate(R.layout.fragment_edit_photo_name_dialog, null);
        builder.setView(dialogView);

        etPhotoName = dialogView.findViewById(R.id.etPhotoName);

        builder.setPositiveButton("Save", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogDataReturn(etPhotoName.getText().toString());
                        Toast.makeText(getContext(), "Photo name has changed", Toast.LENGTH_LONG).show();
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
        View view =  inflater.inflate(R.layout.fragment_edit_photo_name_dialog, container, false);

        return view;
    }
}