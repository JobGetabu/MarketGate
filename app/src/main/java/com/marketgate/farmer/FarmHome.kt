package com.marketgate.farmer


import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
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
class FarmHome : Fragment(), OnRecyclerItemClickListener {

    private val TAG: String = "FarmHome"
    //firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var activity: FarmerActivity
    private lateinit var productAdapter1 : ProductAdapter
    private lateinit var productAdapter2 : SmallProductAdapter
    private lateinit var productAdapter3 : ProductAdapter

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

        initTopList()
        initMiddleList()
        initBottomList()
    }

    private fun initTopList() {

        productAdapter1 = ProductAdapter(context!!,this)
        farmhome_myprodList.adapter = productAdapter1

        firestore.collection(USER_FARMER_Product)
            .whereEqualTo("productid",auth.currentUser?.uid.toString())
            .addSnapshotListener(EventListener { snapshot, firebaseFirestoreException ->
                run {
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                        return@EventListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        Log.d(TAG, "Current data: " + snapshot.documents)
                        val list :List<UserFarmerProduct> = snapshot.toObjects(UserFarmerProduct::class.java)
                        productAdapter1.setItems(list)

                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
            })
    }

    private fun initMiddleList() {

        productAdapter2 = SmallProductAdapter(context!!,this)
        farmhome_recoList.adapter = productAdapter2

        firestore.collection(USER_FARMER_Product)
            .whereEqualTo("productid","recommended")
            .addSnapshotListener(EventListener { snapshot, firebaseFirestoreException ->
                run {
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                        return@EventListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        Log.d(TAG, "Current data: " + snapshot.documents)
                        val list :List<UserFarmerProduct> = snapshot.toObjects(UserFarmerProduct::class.java)
                        productAdapter2.setItems(list)

                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
            })
    }

    private fun initBottomList() {

        productAdapter3 = ProductAdapter(context!!,this)
        farmhome_topList.adapter = productAdapter3

        firestore.collection(USER_FARMER_Product)
            .whereEqualTo("productid","top")
            .addSnapshotListener(EventListener { snapshot, firebaseFirestoreException ->
                run {
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                        return@EventListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        Log.d(TAG, "Current data: " + snapshot.documents)
                        val list :List<UserFarmerProduct> = snapshot.toObjects(UserFarmerProduct::class.java)
                        productAdapter3.setItems(list)

                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
            })
    }

    override fun onItemClick(position: Int) {
        val userFarmerProduct1 = productAdapter1.getItem(position)
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
            obj.productid = auth.currentUser?.uid.toString()
            obj.id = id
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
                    obj.productid = auth.currentUser?.uid.toString()
                    obj.id = id
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
