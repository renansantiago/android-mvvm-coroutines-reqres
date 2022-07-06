package com.reqres.usecase.errors

import com.reqres.data.error.Error

interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}
