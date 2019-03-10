package com.marketgate.farmer

import android.content.Context
import android.view.ViewGroup
import com.leodroidcoder.genericadapter.GenericRecyclerViewAdapter
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.R
import com.marketgate.models.UserFarmerProduct

class SmallProductAdapter(context: Context, listener: OnRecyclerItemClickListener) : GenericRecyclerViewAdapter<UserFarmerProduct, OnRecyclerItemClickListener, SmallProductViewHolder>(context, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallProductViewHolder {
        return SmallProductViewHolder(inflate(R.layout.single_small_product, parent), listener)
    }
}