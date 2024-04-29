package com.example.pentimento;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SecretManger implements editSecretMessageDialogFragment.DialogListener {
    private PhotoActivity myActivity;
    private BottomSheetDialog bottomSheetSecret;
    private String myMessage;
    private Photo myPhoto;
    private secretResult callback;
    private StorageManager storageManager;

    public SecretManger(PhotoActivity activity, Photo photo, secretResult callback) {
        this.myActivity = activity;
        this.myPhoto = photo;
        this.callback = callback;
        initBottomSheetDialog();
        extractMessageFromPhoto();
        storageManager = StorageManager.getInstance();
    }

    public interface secretResult {
        void onSecretAdded(String msg);

        void onSecretDeleted();
    }

    public void initBottomSheetDialog() {
        View bottomSheetView = myActivity.getLayoutInflater().inflate(R.layout.bottom_sheet_secret, null);
        bottomSheetSecret = new BottomSheetDialog(myActivity);
        bottomSheetSecret.setContentView(bottomSheetView);

        ViewGroup viewGroup = (ViewGroup) bottomSheetView;
        setupBottomSheetDialogButtonsListener(viewGroup, bottomSheetSecret);
    }

    public void  showBottomSheetDialog() {
        bottomSheetSecret.show();
    }

    // Listen to click on any of the child elements of the view
    private void setupBottomSheetDialogButtonsListener(ViewGroup viewGroup, BottomSheetDialog bottomSheetDialog) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);

            // Only set listeners to Buttons
            if (child instanceof Button) {
                child.setOnClickListener(v -> {
                    if (v.getId() == R.id.btn_add_secret) {
                        openEditSecretMessageDialogFragment();
                    }
                    else if (v.getId() == R.id.btn_edit_secret) {
                        openEditSecretMessageDialogFragment();
                    }
                    else if (v.getId() == R.id.btn_delete_secret)
                    {
                        deleteSecretMessage();
                    }

                    // Dismiss the BottomSheetDialog
                    bottomSheetDialog.dismiss();
                });
            }
        }
    }

    private Bitmap createBitmapToImage(Photo photo) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(myActivity.getResources(), photo.getPhoto());
        Bitmap bitmap = bitmapDrawable.getBitmap();

        return  bitmap;
    }

    public void addSecretMsgToPhoto(Bitmap image, Photo photo, String msg) {
        ImageLsbManipulation MsgEmbed = new ImageLsbManipulation(msg, image);
        Bitmap bitmapWithMsg = MsgEmbed.EmbedMessageAction();
        photo.setPhoto(bitmapWithMsg);
        callback.onSecretAdded(msg);
    }

    public String getSecretMsgFromPhoto() {
        return myMessage;
    }

    private void extractMessageFromPhoto() {
        ImageLsbManipulation extractMessage = new ImageLsbManipulation(myPhoto.getPhoto());
        myMessage = extractMessage.getMessage();
    }


    private void openEditSecretMessageDialogFragment() {

        myActivity.closeSecretMessage();

        editSecretMessageDialogFragment dialogFragment =
                editSecretMessageDialogFragment.createDialog(myMessage);

        FragmentActivity fragmentActivity = (FragmentActivity) myActivity;

        // Set the listener
        dialogFragment.setDialogListener(this);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "YourDialogFragment");
    }

    private void deleteSecretMessage() {
        ImageLsbManipulation lsbManipulation = new
                ImageLsbManipulation(myPhoto.getPhoto());

        // delete the msg and refresh memory
        myPhoto.setPhoto(lsbManipulation.deleteMessageFromLSB());
        myMessage = null;

        //update image in storage
        updatePhotoInStorage();

        //update the activity the msg deleted
        callback.onSecretDeleted();

        UIAlerts.InfoAlert("Secret Message", "Message deleted", myActivity);
    }

    @Override
    public void onDialogDataReturn(String msg) {
        this.myMessage = msg;
        addSecretMsgToPhoto(createBitmapToImage(myPhoto), myPhoto, myMessage);
        updatePhotoInStorage();
    }

    private void updatePhotoInStorage() {
        storageManager.updateImageInStorage(myPhoto, new StorageActionResult() {
            @Override
            public void onSuccess(Object data) {
                Log.d("LSBManipulation", "onSuccess: " + myPhoto);
            }

            @Override
            public void onError(Exception e) {
                Log.d("LSBManipulation", "onError() returned: " + myPhoto);
            }
        });
    }
}
