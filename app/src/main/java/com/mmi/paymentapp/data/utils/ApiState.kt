package com.mmi.paymentapp.data.utils

sealed class ApiState {
    class Success(val data : List<Unit>) : ApiState()
    class Failure(val msg : Throwable) : ApiState()

    object Loading : ApiState()
    object Empty : ApiState()
}