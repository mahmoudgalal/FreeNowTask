package com.mgalal.freenowtask.data

import retrofit2.Response

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    companion object {
        fun <U> fromResponse(res: Response<U>): Result<U> {
            val body = res.body()
            return if (res.isSuccessful && body != null)
                Success(body)
            else
                Error(Exception(res.message()))
        }
    }
}