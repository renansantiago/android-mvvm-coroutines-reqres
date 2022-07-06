package com.reqres.data.remote

import com.reqres.data.Resource
import com.reqres.data.dto.users.Users

internal interface RemoteDataSource {
    suspend fun requestUsers(page: Int): Resource<Users>
}
