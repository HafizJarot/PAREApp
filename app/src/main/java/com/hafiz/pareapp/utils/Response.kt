package com.hafiz.pareapp.utils

import java.lang.Error

interface SingleResponse<T>{
    fun onSuccess(data : T?)
    fun onFailure(err: Error)
}

interface ArrayResponse<T>{
    fun onSuccess(datas : List<T>?)
    fun onFailure(err: Error)
}