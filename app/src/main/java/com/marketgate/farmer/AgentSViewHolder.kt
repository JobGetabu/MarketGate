package com.marketgate.farmer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.leodroidcoder.genericadapter.BaseViewHolder
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.models.UserAgent
import com.marketgate.utils.showDetails
import com.raiachat.util.loadUrl
import kotlinx.android.synthetic.main.single_small_product.view.*

class AgentSViewHolder(itemView: View, listener: OnRecyclerItemClickListener?) : BaseViewHolder<UserAgent,
        OnRecyclerItemClickListener>(itemView, listener) {

    private val imageV: ImageView? = itemView.s_image
    private val nameTv: TextView? = itemView.s_name


    private var product: UserAgent? = null

    init {
        listener?.run {
            imageV?.setOnClickListener {
                showDetails(imageV.context, product?.userid)
            }
        }
    }

    override fun onBind(product: UserAgent) {
        nameTv?.text = product.name
        imageV?.loadUrl(product.photourl)
        this.product = product
    }

}