package com.marketgate.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

public class DialogUtils {

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    public static void showSettingsDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            //open settings from mnyumba utils
            openSettings(context);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            //todo check and confirm if finish is required here
            //finish();
        });
        builder.show();
    }

    // navigating user to app settings
    private static void openSettings(Context context) {
        Activity myActivity = (Activity) context;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        myActivity.startActivityForResult(intent, 101);
    }
}
