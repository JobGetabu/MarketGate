package com.marketgate.farmer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marketgate.R
import com.marketgate.models.USER_FARMER_Product
import com.marketgate.models.UserFarmerProduct
import com.raiachat.util.loadUrl
import kotlinx.android.synthetic.main.activity_prod_detail.*

class ProdDetailsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "prodD"
        public const val DOC_ID_EXTRA = "DOC_ID_EXTRA"
        private const val PROFILE = "Profile"

        fun newIntent(context: Context): Intent =
            Intent(context, ProdDetailsActivity::class.java)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prod_detail)

        //firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val docid = intent.getStringExtra(DOC_ID_EXTRA)
        Log.d(TAG,"product id = $docid")

        setUpUI(docid!!)
    }

    private fun setUpUI(docid: String) {
        firestore.collection(USER_FARMER_Product).document(docid)
            .get().addOnSuccessListener {
                val prod = it.toObject(UserFarmerProduct::class.java) ?: return@addOnSuccessListener

                d_image.loadUrl(prod.photourl)
                d_title.text = prod.productname
                d_category.text = prod.producttype
                d_price.text = "Kes ${prod.priceindex}.00"
                d_desc.text = prod.productdescription
                d_units.text = "${ prod.units}"
            }
    }
}
