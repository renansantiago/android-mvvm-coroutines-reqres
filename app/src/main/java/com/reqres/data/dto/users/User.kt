package com.reqres.data.dto.users

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int = 0,
    val email: String = "",
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("last_name")
    val lastName: String = "",
    val avatar: String? = null,
    var isFavourite: Boolean? = false
) : Parcelable {
    val fullName: String get() = "$firstName $lastName"
}
