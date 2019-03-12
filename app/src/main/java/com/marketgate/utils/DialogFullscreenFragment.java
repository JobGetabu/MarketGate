package com.marketgate.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.marketgate.R;
import com.marketgate.models.UserFarmerProduct;

import java.io.IOException;
import java.util.List;

public class DialogFullscreenFragment extends DialogFragment {

    private static final int REQUEST_IMAGE = 100;
    public CallbackResult callbackResult;
    private Uri mUri;
    private Bitmap bitmap;
    String title = "";

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    private int request_code = 0;
    private View root_view;

    private ImageView productPic, three_passport2;
    private EditText pName, pDescription, pUnits, pPrice, pType, d_pr_loc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root_view = inflater.inflate(R.layout.dialog_new_farmproduct, container, false);


        productPic = root_view.findViewById(R.id.d_product_pic);
        three_passport2 = root_view.findViewById(R.id.three_passport2);
        pName = root_view.findViewById(R.id.d_prname);
        pDescription = root_view.findViewById(R.id.d_pr_unitdes);
        pPrice = root_view.findViewById(R.id.d_pr_unitprice);
        pUnits = root_view.findViewById(R.id.d_pr_units);
        pType = root_view.findViewById(R.id.d_pr_type);
        d_pr_loc = root_view.findViewById(R.id.d_pr_loc);

        root_view.findViewById(R.id.three_passport2).setOnClickListener(v -> onProfileImageClick());
        root_view.findViewById(R.id.bt_close).setOnClickListener(v -> dismiss());
        root_view.findViewById(R.id.d_save).setOnClickListener(v -> {
            sendDataResult();
            dismiss();
        });


        return root_view;
    }

    private void sendDataResult() {
        String name = pName.getText().toString();
        String picurl = "";
        String descripction = pDescription.getText().toString();
        String type = pType.getText().toString();
        String loc = d_pr_loc.getText().toString();
        int price = 0;
        int units = 0;
        if (!TextUtils.isEmpty(pPrice.getText().toString())) {

            price = Integer.valueOf(pPrice.getText().toString());
        }
        if (!TextUtils.isEmpty(pUnits.getText().toString())) {
            units = Integer.valueOf(pUnits.getText().toString());
        }
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        UserFarmerProduct userFarmerProduct = new UserFarmerProduct(name, type, "", "","", units, price, descripction, userid);

        if (callbackResult != null) {
            if (name.isEmpty()) {
                dismiss();
                return;
            }
            callbackResult.sendResult(request_code, userFarmerProduct, bitmap, title);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }


    public interface CallbackResult {
        void sendResult(int requestCode, UserFarmerProduct obj, Bitmap bitmap, String title);
    }

    private void onProfileImageClick() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        } else {
                            // TODO - handle permission denied case
                            DialogUtils.showSettingsDialog(getContext());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getContext(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    title = MediaStore.Images.Media.TITLE;

                    // loading profile image from local cache
                    loadImage(uri, bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadImage(Uri uri, Bitmap bitmap) {
        mUri = uri;
        productPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        productPic.setImageURI(mUri);
        three_passport2.setVisibility(View.GONE);
    }

}