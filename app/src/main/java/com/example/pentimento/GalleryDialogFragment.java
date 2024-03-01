package com.example.pentimento;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


public class GalleryDialogFragment extends DialogFragment {

    private GridView gvGallery;
    private PhotoAdapter adapter;

    private DialogListener listener;

    public static String TAG = "GalleryDialog";

    public interface DialogListener {
        void onPhotoSelected(Photo selectedImage);
    }

    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the dialog layout for this fragment
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_gallery, null);
        builder.setView(dialogView);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Initialize the gallery and connect to the view
        initGallery(dialogView);

        return builder.create();
    }

    private void initGallery(View dialogView) {
        GalleryManager gm = GalleryManager.getInstance();
        gm.setErrorCallBack(this::errorHandler);

        adapter = new PhotoAdapter(getContext(), gm.getGalleryList(), R.layout.photo_item_grid);
        gm.setPhotoAdapter(adapter);
        gvGallery = dialogView.findViewById(R.id.gvGallery);
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
                listener.onPhotoSelected((Photo)parent.getItemAtPosition(position));
                dismiss();
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_gallery, container, false);

        return view;
    }
}