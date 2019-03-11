package com.marketgate.farmer


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.leodroidcoder.genericadapter.BaseViewHolder
import com.leodroidcoder.genericadapter.GenericRecyclerViewAdapter
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.R
import com.marketgate.models.USER_AGENT
import com.marketgate.models.UserAgent
import com.marketgate.utils.showProfile
import com.raiachat.util.hideView
import com.raiachat.util.loadUrl
import kotlinx.android.synthetic.main.fragment_farm_agency.*
import kotlinx.android.synthetic.main.single_product.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FarmAgency : Fragment(), OnRecyclerItemClickListener {

    private val TAG: String = "FarmHome"
    //firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var activity: FarmerActivity
    private lateinit var productAdapter1 : AgentAdapter
    private lateinit var productAdapter2 : AgentSAdapter
    private lateinit var productAdapter3 : AgentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_farm_agency, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        activity = getActivity() as FarmerActivity

        initTopList()
        initMiddleList()
        initBottomList()
    }


    private fun initTopList() {

        productAdapter1 = AgentAdapter(context!!,this)
        farmhome_myprodList.adapter = productAdapter1

        firestore.collection(USER_AGENT)
            .whereEqualTo("new",true)
            .addSnapshotListener(EventListener { snapshot, firebaseFirestoreException ->
                run {
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                        return@EventListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        Log.d(TAG, "Current data: " + snapshot.documents)
                        val list :List<UserAgent> = snapshot.toObjects(UserAgent::class.java)
                        productAdapter1.setItems(list)

                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
            })
    }

    private fun initMiddleList() {

        productAdapter2 = AgentSAdapter(context!!,this)
        farmhome_recoList.adapter = productAdapter2

        firestore.collection(USER_AGENT)
            .whereEqualTo("recommended",true)
            .addSnapshotListener(EventListener { snapshot, firebaseFirestoreException ->
                run {
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                        return@EventListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        Log.d(TAG, "Current data: " + snapshot.documents)
                        val list :List<UserAgent> = snapshot.toObjects(UserAgent::class.java)
                        productAdapter2.setItems(list)

                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
            })
    }

    private fun initBottomList() {

        productAdapter3 = AgentAdapter(context!!,this)
        farmhome_topList.adapter = productAdapter3

        firestore.collection(USER_AGENT)
            .whereEqualTo("top",true)
            .addSnapshotListener(EventListener { snapshot, firebaseFirestoreException ->
                run {
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                        return@EventListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        Log.d(TAG, "Current data: " + snapshot.documents)
                        val list :List<UserAgent> = snapshot.toObjects(UserAgent::class.java)
                        productAdapter3.setItems(list)

                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
            })
    }

    override fun onItemClick(position: Int) {
        productAdapter1.notifyDataSetChanged()
    }
}

class AgentAdapter(context: Context, listener: OnRecyclerItemClickListener) : GenericRecyclerViewAdapter<UserAgent, OnRecyclerItemClickListener, AgentViewHolder>(context, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        return AgentViewHolder(inflate(R.layout.single_product, parent), listener)
    }
}

class AgentViewHolder(itemView: View, listener: OnRecyclerItemClickListener?) : BaseViewHolder<UserAgent,
        OnRecyclerItemClickListener>(itemView, listener) {

    private val imageV: ImageView? = itemView.s_image
    private val nameTv: TextView? = itemView.s_name
    private val desTv: TextView? = itemView.s_description
    private val menuv: ImageButton? = itemView.s_menu


    private var product: UserAgent? = null

    init {
        listener?.run {
            imageV?.setOnClickListener {
                showProfile(imageV.context, product?.userid)
            }
        }
    }

    override fun onBind(product: UserAgent) {
        nameTv?.text = product.name
        desTv?.text = product.cooperativename

        imageV?.loadUrl(product.photourl)
        menuv?.hideView()
        this.product = product
    }

}
