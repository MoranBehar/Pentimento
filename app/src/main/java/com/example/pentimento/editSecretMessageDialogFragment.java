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

public class editSecretMessageDialogFragment extends DialogFragment {

    EditText etSecret;

    public interface DialogListener {
        void onDialogDataReturn(String secretMsg);
    }
    private DialogListener listener;

    // Method to attach the listener
    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }


    public static editSecretMessageDialogFragment createDialog(String msgToEdit) {
        editSecretMessageDialogFragment fragment = new editSecretMessageDialogFragment();
        Bundle args = new Bundle();
        args.putString("msgToEdit", msgToEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Retrieve the secret message passed to the fragment
        String msgToEdit = getArguments().getString("msgToEdit");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the dialog layout for this fragment
        View dialogView = getActivity().getLayoutInflater().
                inflate(R.layout.fragment_edit_secret_message_dialog, null);
        builder.setView(dialogView);

        etSecret = dialogView.findViewById(R.id.etSecret);
        etSecret.setText(msgToEdit);

        builder.setPositiveButton("Save", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogDataReturn(etSecret.getText().toString());
                        UIAlerts.InfoAlert("Secret Message", "Message was embedded in the photo", dialogView.getContext());
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
        View view = inflater.inflate(R.layout.fragment_edit_secret_message_dialog, container, false);

        return view;
    }
}