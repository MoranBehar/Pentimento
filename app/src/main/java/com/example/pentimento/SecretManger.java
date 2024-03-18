package com.example.pentimento;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SecretManger  {
    private Activity myActivity;
    private BottomSheetDialog bottomSheetSecret;
    public SecretManger(Activity activity) {
        this.myActivity = activity;
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
                        Toast.makeText(myActivity, "add", Toast.LENGTH_SHORT).show();

                    } else if (v.getId() == R.id.btn_edit_secret) {
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
}
