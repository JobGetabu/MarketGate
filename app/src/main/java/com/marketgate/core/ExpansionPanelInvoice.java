package com.marketgate.core;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import com.marketgate.R;
import com.marketgate.models.UserFarmerProduct;
import com.marketgate.utils.AuthUtilKt;
import com.marketgate.utils.Tools;
import com.marketgate.utils.ViewAnimation;

public class ExpansionPanelInvoice extends AppCompatActivity {

    public static final String PROD_EXTRA = "PROD_EXTRA";

    private ImageButton bt_toggle_items, bt_toggle_address, bt_toggle_description;
    private View lyt_expand_items, lyt_expand_address, lyt_expand_description;
    private NestedScrollView nested_scroll_view;

    private TextView date,price,prod_name,prod_price,prod_total,prod_units,prod_des,textView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expansion_panel_invoice);

        initToolbar();
        initComponent();

        UserFarmerProduct product = getIntent().getParcelableExtra(PROD_EXTRA);
        if (product != null) {
            Toast.makeText(this, "" + product.toString(), Toast.LENGTH_SHORT).show();

            setUpUI(product);
        }
    }

    private void setUpUI(UserFarmerProduct product) {

        Long thedate = System.currentTimeMillis();
        String time = Tools.getFormattedTimeEvent(thedate) + ", " +Tools.getFormattedDateSimple(thedate);
        date.setText(time);
        prod_name.setText(product.getProductname());
        prod_des.setText(product.getProductdescription());


        price.setText("Kes "+ (product.getUnits() * product.getPriceindex()));
        prod_total.setText("Kes "+ (product.getUnits() * product.getPriceindex()));
        prod_price.setText("Kes "+ product.getPriceindex());

        prod_units.setText(product.getUnits());


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void initComponent() {

        // nested scrollview
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);

        // section items
        bt_toggle_items = (ImageButton) findViewById(R.id.bt_toggle_items);
        lyt_expand_items = (View) findViewById(R.id.lyt_expand_items);
        date = findViewById(R.id.date);
        price = findViewById(R.id.price);
        prod_name = findViewById(R.id.prod_name);
        prod_price = findViewById(R.id.prod_price);
        prod_total = findViewById(R.id.prod_total);
        prod_units = findViewById(R.id.prod_units);
        prod_des = findViewById(R.id.prod_des);
        textView8 = findViewById(R.id.textView8);


        bt_toggle_items.setOnClickListener(view -> toggleSection(view, lyt_expand_items));

        // section address
        bt_toggle_address = (ImageButton) findViewById(R.id.bt_toggle_address);
        lyt_expand_address = (View) findViewById(R.id.lyt_expand_address);
        bt_toggle_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_address);
            }
        });

        // section description
        bt_toggle_description = (ImageButton) findViewById(R.id.bt_toggle_description);
        lyt_expand_description = (View) findViewById(R.id.lyt_expand_description);
        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description);
            }
        });

        // copy to clipboard
        final TextView tv_invoice_code = (TextView) findViewById(R.id.tv_invoice_code);
        ImageButton bt_copy_code = (ImageButton) findViewById(R.id.bt_copy_code);
        bt_copy_code.setOnClickListener(view -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpansionPanelInvoice.this);
            alertDialogBuilder.setTitle("Invoice").setMessage("Confirm your order invoice").setPositiveButton("Okay", (dialog, which) ->
            {
                Tools.copyToClipboard(getApplicationContext(), tv_invoice_code.getText().toString());
                dialog.dismiss();
                AuthUtilKt.showAlert(ExpansionPanelInvoice.this,"Success","invoice saved");
                new Handler().postDelayed(this::finish,1500);

            }).setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            }).show();
        });

    }


    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, () -> Tools.nestedScrollTo(nested_scroll_view, lyt));
        } else {
            ViewAnimation.collapse(lyt);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invoice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpansionPanelInvoice.this);
            alertDialogBuilder.setTitle("Invoice").setMessage("Confirm your order invoice").setPositiveButton("Okay", (dialog, which) ->
            {

                dialog.dismiss();
                AuthUtilKt.showAlert(ExpansionPanelInvoice.this,"Success","invoice saved");
                new Handler().postDelayed(this::finish,1500);

            }).setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }


}
