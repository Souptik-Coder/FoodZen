package com.example.foody.util

import androidx.annotation.StringRes

sealed class NetworkResults<T>(
    val data: T? = null,
    @StringRes val messageResId: Int? = null
) {
    class Success<T>(data: T) : NetworkResults<T>(data)
    class Error<T>(@StringRes messageResId: Int) : NetworkResults<T>(null, messageResId)
    class Loading<T> : NetworkResults<T>()
}
