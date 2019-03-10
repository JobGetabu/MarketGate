package com.marketgate.farmer


import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.marketgate.R
import com.marketgate.models.USER_FARMER_FILE
import com.marketgate.models.USER_FARMER_Product
import com.marketgate.models.UserFarmerProduct
import com.marketgate.utils.DialogFullscreenFragment
import com.marketgate.utils.showAlert
import kotlinx.android.synthetic.main.fragment_farm_home.*
import java.io.ByteArrayOutputStream

private const val DIALOG_QUEST_CODE = 100

/**
 * A simple [Fragment] subclass.
 *
 */
class FarmHome : Fragment() {

    //firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var activity: FarmerActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_farm_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        activity = getActivity() as FarmerActivity

        farmhome_add.setOnClickListener { showDialogFullscreen() }
    }

    private fun showDialogFullscreen() {
        val fragmentManager = fragmentManager
        val newFragment = DialogFullscreenFragment()
        newFragment.setRequestCode(DIALOG_QUEST_CODE)
        val transaction = fragmentManager!!.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
        newFragment.setOnCallbackResult { requestCode, obj, bitmap, title ->
            if (requestCode == DIALOG_QUEST_CODE) {
                //do some shit
                saveProduct(obj,bitmap,title)
            }
        }
    }

    private fun saveProduct(obj: UserFarmerProduct, bitmap: Bitmap?, title: String) {

        if (bitmap == null){
            val id = firestore.collection(USER_FARMER_Product).document().id
            obj.productid = id
            firestore.collection(USER_FARMER_Product).document(id)
                .set(obj)
                .addOnSuccessListener { showAlert(activity,"Success","Product added") }

        }else{

            val prodImagesRef = FirebaseStorage.getInstance().reference.child(USER_FARMER_FILE+title)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = prodImagesRef
                .putBytes(data)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        showAlert(activity,"Error","Product not uploaded")
                    }
                }
                return@Continuation prodImagesRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val id = firestore.collection(USER_FARMER_Product).document().id
                    obj.productid = id
                    obj.photourl = downloadUri.toString()
                    firestore.collection(USER_FARMER_Product).document(id)
                        .set(obj)
                        .addOnSuccessListener {  showAlert(activity,"Success","Product added") }

                } else {
                    // Handle failures
                    showAlert(activity,"Error","Product not uploaded")
                }
            }
        }
    }

}
