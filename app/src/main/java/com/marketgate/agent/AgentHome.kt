package com.marketgate.agent


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.R
import com.marketgate.models.USER_FARMER
import com.marketgate.models.UserFarmer
import com.raiachat.util.hideView
import kotlinx.android.synthetic.main.fragment_farm_home.*

private const val DIALOG_QUEST_CODE = 100

/**
 * A simple [Fragment] subclass.
 *
 */
class AgentHome : Fragment(), OnRecyclerItemClickListener {

    private val TAG: String = "AgentHome"
    //firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var activity: AgentActivity

    private lateinit var productAdapter2 : FarmerAdapter
    private lateinit var productAdapter3 : FarmerAdapter

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
        activity = getActivity() as AgentActivity

        initView()

        initMiddleList()
        initBottomList()
    }

    private fun initView() {
        topLay.hideView()
        label1.hideView()
        farmhome_add.hideView()
        farmhome_myprodList.hideView()
        label2.text = "New Farmers"
        label3.text = "Top Rated Farmers"
    }

    private fun initMiddleList() {

        productAdapter2 = FarmerAdapter(context!!,this)
        farmhome_recoList.adapter = productAdapter2

        firestore.collection(USER_FARMER)
            .whereEqualTo("new",true)
            .addSnapshotListener(EventListener { snapshot, firebaseFirestoreException ->
                run {
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                        return@EventListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        Log.d(TAG, "Current data: " + snapshot.documents)
                        val list :List<UserFarmer> = snapshot.toObjects(UserFarmer::class.java)
                        productAdapter2.setItems(list)

                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
            })
    }

    private fun initBottomList() {

        productAdapter3 = FarmerAdapter(context!!,this)
        farmhome_topList.adapter = productAdapter3

        firestore.collection(USER_FARMER)
            .whereEqualTo("recommended",true)
            .addSnapshotListener(EventListener { snapshot, firebaseFirestoreException ->
                run {
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                        return@EventListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        Log.d(TAG, "Current data: " + snapshot.documents)
                        val list :List<UserFarmer> = snapshot.toObjects(UserFarmer::class.java)
                        productAdapter3.setItems(list)

                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
            })
    }

    override fun onItemClick(position: Int) {}

}
