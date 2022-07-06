package com.reqres.data.remote.service

import com.reqres.data.dto.users.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersService {
    @GET("users")
    suspend fun fetchUsers(
        @Query(value = "page") page: Int
    ): Response<UserResponse>
}
