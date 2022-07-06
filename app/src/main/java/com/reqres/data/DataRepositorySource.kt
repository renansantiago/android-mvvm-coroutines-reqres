package com.reqres.data

import com.reqres.data.dto.users.Users
import kotlinx.coroutines.flow.Flow

interface DataRepositorySource {
    suspend fun requestUsers(page: Int): Flow<Resource<Users>>
    suspend fun addToFavourite(id: Int): Flow<Resource<Boolean>>
    suspend fun removeFromFavourite(id: Int): Flow<Resource<Boolean>>
    suspend fun isFavourite(id: Int): Flow<Resource<Boolean>>
}
