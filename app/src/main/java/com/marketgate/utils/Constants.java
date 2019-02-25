package com.marketgate.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by Job on Friday : 1/4/2019.
 */
public class Constants {
    public static final String IMAGE_SAVE_CLICKS = "IMAGE_SAVE_CLICKS";

    public static void openSettings(Context context) {
        Activity myActivity = (Activity) context;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        myActivity.startActivityForResult(intent, 101);
    }

}
