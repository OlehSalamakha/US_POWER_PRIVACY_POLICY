package com.example.uspower.core

sealed class LoadState<out T> {
    data class Success<T>(val data: T): LoadState<T>()
    data class Error(val throws: Throwable): LoadState<Nothing>()
    data object Loading: LoadState<Nothing>()
}
