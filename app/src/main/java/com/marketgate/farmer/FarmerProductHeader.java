package com.marketgate.farmer;


import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marketgate.R;
import com.marketgate.models.UserFarmerProduct;
import com.marketgate.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import static com.marketgate.core.ProfileActivity.USER_ID_EXTRA;
import static com.marketgate.models.FirebaseRefsKt.USER_FARMER_Product;

public class FarmerProductHeader extends AppCompatActivity {

    private View parent_view;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private List<UserFarmerProduct> userFarmerProductList = new ArrayList<>();

    private TextView d_title , d_category , d_price, d_desc, d_units;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_header);

        initToolbar();

        initComponent();

        String docid = getIntent().getStringExtra(USER_ID_EXTRA);
        initList(docid);
    }

    private void initList(String docid) {
        FirebaseFirestore.getInstance().collection(USER_FARMER_Product)
                .whereEqualTo("userid",docid)
                .get()
                .addOnCompleteListener(task -> userFarmerProductList = task.getResult().toObjects(UserFarmerProduct.class));
        adapterImageSlider.notifyDataSetChanged();
    }

    private void initToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, userFarmerProductList);

        d_title = findViewById(R.id.d_title);
        d_category = findViewById(R.id.d_category);
        d_price = findViewById(R.id.d_price);
        d_desc = findViewById(R.id.d_desc);
        d_units = findViewById(R.id.d_units);


        adapterImageSlider.setItems(userFarmerProductList);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {

                changeViewContent(pos);
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void changeViewContent(int pos) {
        UserFarmerProduct prod = userFarmerProductList.get(pos);
        if (prod != null){

            d_title.setText(prod.getProductname());
            d_category.setText(prod.getProducttype());
            d_price.setText("Kes $"+ String.valueOf(prod.getPriceindex())+ ".00");
            d_desc.setText(prod.getProductdescription());
            d_units.setText(prod.getUnits());
        }
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(ContextCompat.getColor(this, R.color.overlay_dark_10), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<UserFarmerProduct> userFarmerProducts;

        private OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<UserFarmerProduct> userFarmerProducts) {
            this.act = activity;
            this.userFarmerProducts = userFarmerProducts;
        }

        @Override
        public int getCount() {
            return this.userFarmerProducts.size();
        }

        public UserFarmerProduct getItem(int pos) {
            return userFarmerProducts.get(pos);
        }

        public void setItems(List<UserFarmerProduct>  items) {
            this.userFarmerProducts = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final String imageurl = userFarmerProducts.get(position).getPhotourl();

            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
            Tools.displayImageOriginal(act, image, imageurl);
            lyt_parent.setOnClickListener(v1 -> {
                if (onItemClickListener != null) {
                    //onItemClickListener.onItemClick(v1, o);
                }
            });

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }
}
