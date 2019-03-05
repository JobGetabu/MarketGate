package com.marketgate.farmer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.marketgate.R
import com.marketgate.utils.DialogFullscreenFragment
import kotlinx.android.synthetic.main.fragment_farm_home.*

private const val DIALOG_QUEST_CODE = 100

/**
 * A simple [Fragment] subclass.
 *
 */
class FarmHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_farm_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        farmhome_add.setOnClickListener { showDialogFullscreen() }
    }

    private fun showDialogFullscreen() {
        val fragmentManager = fragmentManager
        val newFragment = DialogFullscreenFragment()
        newFragment.setRequestCode(DIALOG_QUEST_CODE)
        val transaction = fragmentManager!!.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
        newFragment.setOnCallbackResult(DialogFullscreenFragment.CallbackResult { requestCode, obj ->
            if (requestCode == DIALOG_QUEST_CODE){
                //do some shit
            }
        })
    }

}
