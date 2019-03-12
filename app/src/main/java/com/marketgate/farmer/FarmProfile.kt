package com.marketgate.farmer


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.marketgate.R
import com.marketgate.models.USER_FARMER
import com.marketgate.models.USER_FARMER_PICK
import com.marketgate.models.UserFarmer
import com.marketgate.utils.ImagePickerActivity
import com.marketgate.utils.showAlert
import com.raiachat.util.loadUrl
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 *
 */


class FarmProfile : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var activity: FarmerActivity

    private val REQUEST_IMAGE = 100
    private lateinit var bitmap: Bitmap
    private var mUri: Uri? = null
    private var title = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        activity = getActivity() as FarmerActivity

        setUI()

        profile_btn_update.setOnClickListener { updateUser() }
        profile_image.setOnClickListener { showImagePickerOptions() }
    }

    private fun setUI() {
        val user = auth.currentUser

        firestore.collection(USER_FARMER).document(user!!.uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val userFarmer = it.result!!.toObject(UserFarmer::class.java) ?: return@addOnCompleteListener

                    profile_image.loadUrl(userFarmer.photourl)
                    profile_fullname.editText!!.setText(userFarmer.name)
                    profile_coop.editText!!.setText(userFarmer.cooperativename)
                    profile_location.editText!!.setText(userFarmer.locationstring)
                    profile_selling.isChecked = userFarmer.sellingstatus
                }
            }
    }

    private fun updateUser() {
        val user = auth.currentUser

        if (mUri != null) {

            val profImagesRef = FirebaseStorage.getInstance().reference.child(USER_FARMER_PICK + title)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = profImagesRef
                .putBytes(data)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        showAlert(activity, "Error", "Profile not uploaded")
                    }
                }
                return@Continuation profImagesRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    firestore.collection(USER_FARMER).document(user!!.uid)
                        .update(
                            "name", profile_fullname.editText!!.text.toString(),
                            "sellingstatus", profile_selling.isChecked,
                            "locationstring", profile_location.editText!!.text.toString(),
                            "photourl", downloadUri.toString(),
                            "cooperativename", profile_coop.editText!!.text.toString()
                        )
                        .addOnSuccessListener {
                            showAlert(activity, "Success", "Profile updated")
                        }
                } else {
                    // Handle failures
                    showAlert(activity, "Error", "Product not uploaded")
                }
            }

        } else {

            firestore.collection(USER_FARMER).document(user!!.uid)
                .update(
                    "name", profile_fullname.editText!!.text.toString(),
                    "sellingstatus", profile_selling.isChecked,
                    "locationstring", profile_location.editText!!.text.toString(),
                    "cooperativename", profile_coop.editText!!.text.toString()
                )
                .addOnSuccessListener {
                    showAlert(activity, "Success", "Profile updated")
                }
        }

    }

    private fun launchCameraIntent() {
        val intent = Intent(context, ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE)

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)

        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(context, ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE)

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // You can update this bitmap to your server
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity()!!.contentResolver, uri)
                    title = MediaStore.Images.Media.TITLE

                    // loading profile image from local cache
                    loadImage(uri, bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun loadImage(uri: Uri, bitmap: Bitmap) {
        mUri = uri
        profile_image.setImageURI(mUri)

    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(context, object : ImagePickerActivity.PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }
        })
    }
}
