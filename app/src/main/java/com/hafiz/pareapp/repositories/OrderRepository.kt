package com.hafiz.pareapp.repositories

import com.google.gson.GsonBuilder
import com.hafiz.pareapp.models.CreateOrder
import com.hafiz.pareapp.models.Order
import com.hafiz.pareapp.utils.SingleResponse
import com.hafiz.pareapp.webservices.ApiService
import com.hafiz.pareapp.webservices.WrappedListResponse
import com.hafiz.pareapp.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Error

interface OrderContract{
    fun createOrder(token: String, createOrder: CreateOrder, listener : SingleResponse<CreateOrder>)
}

class OrderRepository (private val api : ApiService) : OrderContract{
    override fun createOrder(token: String, createOrder: CreateOrder, listener: SingleResponse<CreateOrder>) {
        val g = GsonBuilder().create()
        val json = g.toJson(createOrder)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.createOrder(token, body).enqueue(object : Callback<WrappedResponse<CreateOrder>>{
            override fun onFailure(call: Call<WrappedResponse<CreateOrder>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<CreateOrder>>, response: Response<WrappedResponse<CreateOrder>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!) listener.onSuccess(b.data) else listener.onFailure(Error(b.message))
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    fun orderStore(token : String, createOrder: CreateOrder, result : (Boolean, Error?)->Unit){
        val g = GsonBuilder().create()
        val json = g.toJson(createOrder)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.createOrder(token, body).enqueue(object : Callback<WrappedResponse<CreateOrder>>{
            override fun onFailure(call: Call<WrappedResponse<CreateOrder>>, t: Throwable) {
                result(false, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<CreateOrder>>, response: Response<WrappedResponse<CreateOrder>>) {
                if (response.isSuccessful){
                    val body  = response.body()
                    if (body?.status!!){
                        result(true, null)
                    }else{
                        result(false, Error(body.message))
                    }
                }else{
                    println(response.message())
                    result(false, Error(response.message()))
                }
            }
        })
    }

    fun getPenyewaMyOrders(token: String, result : (List<Order>?, Error?)-> Unit){
        api.getPenyewaMyOrders(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error())
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }

        })
    }

    fun getPemilikMyOrders(token: String, result: (List<Order>?, Error?) -> Unit){
        api.getPemilikMyOrders(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                println(t.message)
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        println(data)
                        result(data, null)
                    }else{
                        result(null, Error())
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }

        })
    }

}