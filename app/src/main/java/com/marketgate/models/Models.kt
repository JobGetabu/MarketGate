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
    val productname: String = "",
    val producttype: String = "",
    val productid: String = "",
    val photourl: List<String> = emptyList(),
    val units: Number = 0,
    val priceindex: Number = 0,
    var productdescription: String = "",
    var userid: String = ""
)
