package com.marketgate.farmer

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.leodroidcoder.genericadapter.BaseViewHolder
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.models.UserFarmerProduct
import com.marketgate.utils.showDetails
import com.marketgate.utils.showPopup
import com.raiachat.util.hideView
import com.raiachat.util.loadUrl
import kotlinx.android.synthetic.main.single_product.view.*

class ProductViewHolder(itemView: View, listener: OnRecyclerItemClickListener?) : BaseViewHolder<UserFarmerProduct,
        OnRecyclerItemClickListener>(itemView, listener) {

    private val imageV: ImageView? = itemView.s_image
    private val nameTv: TextView? = itemView.s_name
    private val nameDesTv: TextView? = itemView.s_description
    private val menuV: ImageButton? = itemView.s_menu

    private var product: UserFarmerProduct? = null

    init {
        listener?.run {
            imageV?.setOnClickListener {
                showDetails(imageV.context, product?.id)
            }

            menuV?.setOnClickListener {
                showPopup(menuV, menuV.context, product?.id)
                onItemClick(adapterPosition)
            }
        }
    }

    override fun onBind(product: UserFarmerProduct) {
        nameTv?.text = product.productname
        nameDesTv?.text = product.productdescription
        imageV?.loadUrl(product.photourl)
        this.product = product

        if (product.productid == "top") menuV?.hideView()
    }

}