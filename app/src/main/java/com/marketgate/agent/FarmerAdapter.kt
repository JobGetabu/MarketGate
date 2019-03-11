package com.marketgate.agent

import android.content.Context
import android.view.ViewGroup
import com.leodroidcoder.genericadapter.GenericRecyclerViewAdapter
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.R
import com.marketgate.models.UserFarmer

class FarmerAdapter(context: Context, listener: OnRecyclerItemClickListener) : GenericRecyclerViewAdapter<UserFarmer, OnRecyclerItemClickListener, FarmerViewHolder>(context, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmerViewHolder {
        return FarmerViewHolder(inflate(R.layout.single_product, parent), listener)
    }
}