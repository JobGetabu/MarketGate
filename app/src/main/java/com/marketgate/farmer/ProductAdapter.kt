package com.marketgate.farmer

import android.content.Context
import android.view.ViewGroup
import com.leodroidcoder.genericadapter.GenericRecyclerViewAdapter
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.R
import com.marketgate.models.UserFarmerProduct

class ProductAdapter(context: Context, listener: OnRecyclerItemClickListener) : GenericRecyclerViewAdapter<UserFarmerProduct, OnRecyclerItemClickListener, ProductViewHolder>(context, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(inflate(R.layout.single_product, parent), listener)
    }
}