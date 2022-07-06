package com.reqres.data.remote

import com.reqres.data.Resource
import com.reqres.data.dto.users.Users
import com.reqres.data.dto.users.UserResponse
import com.reqres.data.error.NETWORK_ERROR
import com.reqres.data.remote.service.UsersService
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteData @Inject
constructor(private val serviceGenerator: ServiceGenerator) : RemoteDataSource {
    override suspend fun requestUsers(page: Int): Resource<Users> {
        val service = serviceGenerator.createService(UsersService::class.java)
        return when (val response = processCall { service.fetchUsers(page) }) {
            is UserResponse -> {
                Resource.Success(data = Users(ArrayList(response.data)))
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }
}
