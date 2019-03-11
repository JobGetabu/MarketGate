package com.marketgate.farmer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.leodroidcoder.genericadapter.BaseViewHolder
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.models.UserFarmerProduct
import com.marketgate.utils.showDetails
import com.raiachat.util.loadUrl
import kotlinx.android.synthetic.main.single_small_product.view.*

class SmallProductViewHolder(itemView: View, listener: OnRecyclerItemClickListener?) : BaseViewHolder<UserFarmerProduct,
        OnRecyclerItemClickListener>(itemView, listener) {

    private val imageV: ImageView? = itemView.s_image
    private val nameTv: TextView? = itemView.s_name


    private var product: UserFarmerProduct? = null

    init {
        listener?.run {
            imageV?.setOnClickListener {
                showDetails(imageV.context, product?.id)
            }
        }
    }

    override fun onBind(product: UserFarmerProduct) {
        nameTv?.text = product.productname
        imageV?.loadUrl(product.photourl)
        this.product = product
    }

}