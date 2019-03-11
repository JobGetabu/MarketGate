package com.marketgate.farmer

import android.content.Context
import android.view.ViewGroup
import com.leodroidcoder.genericadapter.GenericRecyclerViewAdapter
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.marketgate.R
import com.marketgate.models.UserAgent

class AgentSAdapter(context: Context, listener: OnRecyclerItemClickListener) : GenericRecyclerViewAdapter<UserAgent, OnRecyclerItemClickListener, AgentSViewHolder>(context, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentSViewHolder {
        return AgentSViewHolder(inflate(R.layout.single_small_product, parent), listener)
    }
}