package com.marketgate.agent

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.leodroidcoder.genericadapter.BaseViewHolder
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.models.UserFarmer
import com.marketgate.utils.showDetails
import com.raiachat.util.hideView
import com.raiachat.util.loadUrl
import kotlinx.android.synthetic.main.single_product.view.*

class FarmerViewHolder(itemView: View, listener: OnRecyclerItemClickListener?) : BaseViewHolder<UserFarmer,
        OnRecyclerItemClickListener>(itemView, listener) {

    private val imageV: ImageView? = itemView.s_image
    private val nameTv: TextView? = itemView.s_name
    private val nameDesTv: TextView? = itemView.s_description
    private val menuV: ImageButton? = itemView.s_menu

    private var product: UserFarmer? = null

    init {
        listener?.run {
            imageV?.setOnClickListener {
                showDetails(imageV.context, product?.userid)
            }

        }
    }

    override fun onBind(product: UserFarmer) {
        nameTv?.text = product.name
        nameDesTv?.text = product.cooperativename
        imageV?.loadUrl(product.photourl)
        this.product = product
        menuV?.hideView()
    }

}