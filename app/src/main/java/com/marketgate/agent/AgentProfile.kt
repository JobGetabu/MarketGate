package com.marketgate.agent


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marketgate.R
import com.marketgate.models.USER_FARMER
import com.marketgate.models.UserFarmer
import com.marketgate.utils.showAlert
import com.raiachat.util.loadUrl
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 *
 */


class AgentProfile : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var activity: AgentActivity

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
        activity = getActivity() as AgentActivity

        setUI()

        profile_btn_update.setOnClickListener { updateUser() }
    }

    private fun setUI() {
        val user = auth.currentUser

        profile_selling.text = "Are you procuring farmers"

        firestore.collection(USER_FARMER).document(user!!.uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val userFarmer  = it.result!!.toObject(UserFarmer::class.java) ?: return@addOnCompleteListener

                    profile_image.loadUrl(userFarmer.photourl)
                    profile_fullname.editText!!.setText( userFarmer.name)
                    profile_coop.editText!!.setText( userFarmer.cooperativename)
                    profile_location.editText!!.setText( userFarmer.locationstring)
                    profile_selling.isChecked = userFarmer.sellingstatus
                }
            }
    }

    private fun updateUser() {
        val user = auth.currentUser

        firestore.collection(USER_FARMER).document(user!!.uid)
            .update("name",profile_fullname.editText!!.text.toString(),
                "sellingstatus",profile_selling.isChecked,
                "locationstring",profile_location.editText!!.text.toString(),
            "cooperativename",profile_coop.editText!!.text.toString())
            .addOnSuccessListener {
                showAlert(activity,"Success","Profile updated")
            }
    }


}
