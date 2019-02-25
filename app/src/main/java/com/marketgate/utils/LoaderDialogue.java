package com.marketgate.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.marketgate.R;

public class LoaderDialogue extends DialogFragment {

    public static String TAG = "FullScreenDialog";
    public static String DIA_TITLE = "DIA_TITLE";
    public static String DIA_TXT = "DIA_TXT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_full_screen_dialog, container, false);

        TextView title = view.findViewById(R.id.dia_title);
        TextView txt = view.findViewById(R.id.dia_txt);

        Bundle b = getArguments();
        if (b != null) {

            String titletxt = b.getString("DIA_TITLE", "Loading...");
            String subtitle = b.getString("DIA_TXT", "");

            title.setText(titletxt);
            txt.setText(subtitle);
        }

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
