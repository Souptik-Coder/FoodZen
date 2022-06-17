package com.example.foody.util

import androidx.annotation.StringRes

sealed class NetworkResults<T>(
    val data: T? = null,
    @StringRes val messageResId: Int? = null ,
    val isErrorHandled: Boolean = false
) {
    class Success<T>(data: T) : NetworkResults<T>(data)
    class Error<T>(@StringRes messageResId: Int, isErrorHandled: Boolean = false) :
        NetworkResults<T>(null, messageResId,isErrorHandled)

    class Loading<T> : NetworkResults<T>()
}
