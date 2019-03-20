package com.marketgate.admin;

import android.content.Context;
import android.content.res.TypedArray;
import com.marketgate.R;
import com.marketgate.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("ResourceType")
public class DataGenerator {

    private static Random r = new Random();

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }

    public static List<String> getStringsShort(Context ctx) {
        List<String> items = new ArrayList<>();
        String name_arr[] = ctx.getResources().getStringArray(R.array.strings_short);
        for (String s : name_arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }


    public static List<String> getStringsMonth(Context ctx) {
        List<String> items = new ArrayList<>();
        String arr[] = ctx.getResources().getStringArray(R.array.month);
        for (String s : arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }




    /**
     * Generate dummy data inbox
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<Inbox> getInboxData(Context ctx) {
        List<Inbox> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        String date_arr[] = ctx.getResources().getStringArray(R.array.general_date);

        for (int i = 0; i < drw_arr.length(); i++) {
            Inbox obj = new Inbox();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.from = name_arr[i];
            obj.email = Tools.getEmailFromName(obj.from);
            obj.message = "Profile details for the user in Market gate backend database overhead";
            obj.date = date_arr[randInt(date_arr.length - 1)];
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }


    public static String formatTime(long time) {
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if (date.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)) {
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            } else {
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        } else {
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }

    private static int getRandomIndex(int max) {
        return r.nextInt(max - 1);
    }
}
