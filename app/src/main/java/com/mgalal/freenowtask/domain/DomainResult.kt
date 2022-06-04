package com.mgalal.freenowtask.domain

sealed class DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Error(val exception: Exception) : DomainResult<Nothing>()
}

