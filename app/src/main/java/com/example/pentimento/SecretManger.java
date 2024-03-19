package com.example.pentimento;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SecretManger implements editSecretMessageDialogFragment.DialogListener {
    private Activity myActivity;
    private BottomSheetDialog bottomSheetSecret;
    private String myMessage;
    private Photo myPhoto;
    public SecretManger(Activity activity, Photo photo) {
        this.myActivity = activity;
        this.myPhoto = photo;
        initBottomSheetDialog();
    }

    public void initBottomSheetDialog() {
        View bottomSheetView = myActivity.getLayoutInflater().inflate(R.layout.bottom_sheet_secret, null);
        bottomSheetSecret = new BottomSheetDialog(myActivity);
        bottomSheetSecret.setContentView(bottomSheetView);

        ViewGroup viewGroup = (ViewGroup) bottomSheetView;
        setupBottomSheetDialogButtonsListener(viewGroup, bottomSheetSecret);
    }

    public void showBottomSheetDialog() {
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
                        //TODO - edit function (the prev name will be written,
                        // the uer will need to change it)

                        Toast.makeText(myActivity, "edit", Toast.LENGTH_SHORT).show();

                    }
                    else if (v.getId() == R.id.btn_delete_secret)
                    {
                        Toast.makeText(myActivity, "delete", Toast.LENGTH_SHORT).show();

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

    private void addSecretMsgToPhoto(Bitmap image, Photo photo, String msg) {
        ImageLsbManipulation MsgEmbed = new ImageLsbManipulation(msg, image);
        Bitmap bitmapWithMsg = MsgEmbed.EmbedMessageAction();
        photo.setPhoto(bitmapWithMsg);
    }

    public String getSecretMsgFromPhoto() {
        ImageLsbManipulation extractMessage = new ImageLsbManipulation(myPhoto.getPhoto());
        return extractMessage.getMessage();
    }


    private void openEditSecretMessageDialogFragment() {
        editSecretMessageDialogFragment dialogFragment = new editSecretMessageDialogFragment();

        FragmentActivity fragmentActivity = (FragmentActivity) myActivity;

        // Set the listener
        dialogFragment.setDialogListener(this);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "YourDialogFragment");
    }

    @Override
    public void onDialogDataReturn(String msg) {
        this.myMessage = msg;
        addSecretMsgToPhoto(createBitmapToImage(myPhoto), myPhoto, myMessage);
    }
}
