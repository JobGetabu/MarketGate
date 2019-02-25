package com.marketgate.models

import com.google.firebase.firestore.GeoPoint


data class UserFarmer(
    val name: String = "",
    val sellingstatus: Boolean = false,
    val cooperativename: String = "",
    val photourl: String = "",
    var email: String = "",
    var phone: String = "",
    var listofactivity: List<String> = emptyList(),
    var locationstring: String = "",
    var location: GeoPoint? = null,
    var userid: String = ""
)

data class UserFarmerProduct(
    val producttype: String = "",
    val productid: String = "",
    val photourl: List<String> = emptyList(),
    val priceindex: Number = 0,
    var userid: String = ""
)
