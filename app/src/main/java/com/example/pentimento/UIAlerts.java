package com.example.pentimento;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

//-----------------------------------------------------------------------------
// Allows opening of an alert everywhere at the code
//-----------------------------------------------------------------------------
public class UIAlerts {

    // Display error alert on the screen
    public static void ErrorAlert(String title, String message, Context page) {
        ShowAlert(title, message, page,  R.style.CustomAlertDialogError);
    }

    // Display informative alert on the screen
    public static void InfoAlert(String title, String message, Context page) {
        ShowAlert(title, message, page,  R.style.CustomAlertDialogInfo);
    }

    // Create and show dialog alert
    private static void ShowAlert(String title, String message, Context page, int styleId)
    {
        // Get Builder
        AlertDialog.Builder builder1 = new AlertDialog.Builder(page, styleId);

        // Set texts
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        // Set "Okay" button
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // Create the Dialog
        AlertDialog alert11 = builder1.create();

        // Show it.
        alert11.show();
    }
}

