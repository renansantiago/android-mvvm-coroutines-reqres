package com.reqres

import com.reqres.TestUtil.dataStatus
import com.reqres.TestUtil.initData
import com.reqres.data.DataRepositorySource
import com.reqres.data.Resource
import com.reqres.data.dto.users.Users
import com.reqres.data.error.NETWORK_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestDataRepository @Inject constructor() : DataRepositorySource {

    override suspend fun requestUsers(page: Int): Flow<Resource<Users>> {
        return when (dataStatus) {
            DataStatus.Success -> {
                flow { emit(Resource.Success(initData())) }
            }
            DataStatus.Fail -> {
                flow { emit(Resource.DataError<Users>(errorCode = NETWORK_ERROR)) }
            }
            DataStatus.EmptyResponse -> {
                flow { emit(Resource.Success(Users(arrayListOf()))) }
            }
        }
    }

    override suspend fun addToFavourite(id: Int): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }

    override suspend fun removeFromFavourite(id: Int): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }

    override suspend fun isFavourite(id: Int): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }
}
