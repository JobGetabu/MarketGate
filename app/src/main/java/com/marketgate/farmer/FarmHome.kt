package com.marketgate.farmer


import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.marketgate.R
import com.marketgate.models.USER_FARMER_FILE
import com.marketgate.models.USER_FARMER_Product
import com.marketgate.models.UserFarmerProduct
import com.marketgate.utils.DialogFullscreenFragment
import com.marketgate.utils.showShortSnackbar
import kotlinx.android.synthetic.main.fragment_farm_home.*

private const val DIALOG_QUEST_CODE = 100

/**
 * A simple [Fragment] subclass.
 *
 */
class FarmHome : Fragment() {

    //firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

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
            firestore.collection(USER_FARMER_Product).document(auth.currentUser!!.uid)
                .set(obj)
                .addOnSuccessListener { showShortSnackbar("Product added",farmhome_main) }

        }else{

            val mountainImagesRef = FirebaseStorage.getInstance().reference.child(USER_FARMER_FILE+title)
        }
    }

}
