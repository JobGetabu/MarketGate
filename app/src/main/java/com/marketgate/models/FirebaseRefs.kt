package com.marketgate.models

import com.google.firebase.auth.FirebaseAuth

const val DEFAULT_QUERY_LIMIT = 10L
const val USER_FARMER = "UserFarmer"
const val USER_FARMER_Product = "UserFarmerProduct"

 val USER_FARMER_FILE = "farmerproduct/${FirebaseAuth.getInstance().currentUser!!.uid}/"
 val USER_AGENT_FILE = "agentproduct/${FirebaseAuth.getInstance().currentUser!!.uid}/"
 val USER_AGROVET_FILE = "agrovetproduct/${FirebaseAuth.getInstance().currentUser!!.uid}/"
