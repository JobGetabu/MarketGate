package com.marketgate.models

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize


data class UserFarmer(
    val name: String? = "",
    val sellingstatus: Boolean = false,
    val top: Boolean = false,
    val recommended: Boolean = false,
    val new: Boolean = true,
    val cooperativename: String? = "",
    val photourl: String? = "",
    var email: String = "",
    var phone: String? = "",
    var listofactivity: List<String>? = emptyList(),
    var locationstring: String? = "",
    var location: GeoPoint? = null,
    var userid: String = ""
)

data class UserAgrovet(
    val name: String? = "",
    val buyingstatus: Boolean = false,
    val top: Boolean = false,
    val recommended: Boolean = false,
    val new: Boolean = true,
    val cooperativename: String? = "",
    val photourl: String? = "",
    var email: String = "",
    var phone: String? = "",
    var listofactivity: List<String>? = emptyList(),
    var locationstring: String? = "",
    var userid: String = ""
)

data class UserAgent(
    val name: String? = "",
    val buyingstatus: Boolean = false,
    val top: Boolean = false,
    val recommended: Boolean = false,
    val new: Boolean = true,
    val cooperativename: String? = "",
    val photourl: String? = "",
    var email: String = "",
    var phone: String? = "",
    var listofactivity: List<String>? = emptyList(),
    var locationstring: String? = "",
    var userid: String = ""
)

data class UserAdmin(
    val name: String? = "",
    val photourl: String? = "",
    var email: String = "",
    var role: String? = "Admin",
    var userid: String = ""
)

@Parcelize
data class UserFarmerProduct(
    val productname: String = "",
    val producttype: String? = "",
    var productid: String = "",
    var id: String = "",
    var photourl: String = "",
    val units: Int = 0,
    val priceindex: Int = 0,
    var productdescription: String = "",
    var userid: String = ""
): Parcelable
