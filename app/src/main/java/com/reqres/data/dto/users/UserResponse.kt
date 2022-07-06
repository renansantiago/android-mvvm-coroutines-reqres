package com.reqres.data.dto.users

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val data: List<User>
)
