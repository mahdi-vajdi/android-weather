package com.mahdivajdi.myweather2.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import retrofit2.Response


sealed class ResultData<out T> {

    data class Success<out T>(val value: T) : ResultData<T>()

    data class Failure<out T>(val message: String) : ResultData<T>()

    companion object {
        fun <T> success(value: T): ResultData<T> = Success(value)
        fun <T> failure(error_msg: String): ResultData<T> = Failure(error_msg)
    }
}


abstract class BaseRemoteDataSource {

    protected suspend fun <T> getData(call: suspend () -> Response<T>): ResultData<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ResultData.success(body)
            }
            return formatError(" ${response.code()} ${response.message()}")
        } catch (exception: Exception) {
            return formatError(exception.message)
        }
    }

    private fun <T> formatError(errorMessage: String?): ResultData<T> {
        return ResultData.failure("Network call has failed for a following reason: $errorMessage")
    }
}