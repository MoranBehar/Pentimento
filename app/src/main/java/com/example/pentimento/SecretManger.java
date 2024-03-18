package com.example.pentimento;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SecretManger {
    private Activity myActivity;
    private BottomSheetDialog bottomSheetSecret;

    private String myMessage;
    private Photo myPhoto;
    public SecretManger(Activity activity, Photo photo, String msg) {
        this.myActivity = activity;
        this.myPhoto = photo;
        myMessage = msg;
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
                        addSecretMsgToPhoto(createBitmapToImage(myPhoto), myPhoto, myMessage);

                        Toast.makeText(myActivity, "add", Toast.LENGTH_SHORT).show();

                    } else if (v.getId() == R.id.btn_edit_secret) {
                        getSecretMsgFromPhoto(createBitmapToImage(myPhoto));
                        //TODO - edit function

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

//        Bitmap myPhotoBitmap = photo.getPhoto();
        return  bitmap;
    }

    private void addSecretMsgToPhoto(Bitmap image, Photo photo, String msg) {
        ImageLsbManipulation MsgEmbed = new ImageLsbManipulation(msg, image);
        Bitmap bitmapWithMsg = MsgEmbed.EmbedMessageAction();
        photo.setPhoto(bitmapWithMsg);
    }

    private void getSecretMsgFromPhoto(Bitmap image) {
        ImageLsbManipulation extractMessage = new ImageLsbManipulation(image);
        extractMessage.getMassage();
    }
}
