package com.marketgate.core;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.marketgate.R;
import com.marketgate.utils.AuthUtilKt;
import com.marketgate.utils.LoaderDialogue;
import com.marketgate.utils.Tools;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import static com.marketgate.utils.LoaderDialogue.DIA_TITLE;
import static com.marketgate.utils.LoaderDialogue.DIA_TXT;

public class FormCheckout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_checkout);
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void initComponent() {
        TextInputLayout myname = findViewById(R.id.myname);
        myname.getEditText().setText(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));

        (findViewById(R.id.bt_exp_date)).setOnClickListener(v -> dialogDatePickerLight(v));

        (findViewById(R.id.bt_submit)).setOnClickListener(this::simulateRequest);
    }

    private void dialogDatePickerLight(final View v) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    long date = calendar.getTimeInMillis();
                    ((EditText) v).setText(Tools.getFormattedDateShort(date));
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMinDate(cur_calender);
        datePicker.show(getFragmentManager(), "Expiration Date");
    }

    private void requestSaving() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Send Request").setMessage("Confirm sending this information to the agent. This will be facilitated by Market Gate to make sure you get the best market for your products")
                .setPositiveButton("Send", (dialog, which) -> {
                    dialog.dismiss();
                    simulateRequest(findViewById(R.id.bt_exp_date));
                }).setNegativeButton("Not now", (dialog, which) -> {
            dialog.dismiss();
        }).show();
    }

    public void simulateRequest(View v) {

        LoaderDialogue loader = new LoaderDialogue();
        showProgress("Please wait", "Sending request...", loader);
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (loader.isAdded() && loader.isVisible())
                    loader.dismiss();

                AuthUtilKt.showAlert(FormCheckout.this,"Success","request sent");
                new Handler().postDelayed(FormCheckout.this::finish,1500);
            }
        }.start();
    }

    private void showProgress(String title, String txt, LoaderDialogue loader) {

        Bundle b = new Bundle();
        b.putString(DIA_TITLE, title);
        b.putString(DIA_TXT, txt);

        loader.setArguments(b);

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        loader.show(tr, LoaderDialogue.TAG);
    }

}
