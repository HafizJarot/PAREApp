package com.hafiz.pareapp.repositories

import com.hafiz.pareapp.models.Order
import com.hafiz.pareapp.webservices.ApiService
import com.hafiz.pareapp.webservices.WrappedListResponse
import com.hafiz.pareapp.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Error

class OrderRepository (private val api : ApiService){

    fun orderStore(token : String, id_penyewa : String, id_produk : String, harga : String,
                   tanggal_mulai_sewa : String, selesai_sewa : String, sisi : String, result : (Boolean, Error?)->Unit){

        api.orderStore(token, id_penyewa.toInt(), id_produk.toInt(), harga.toInt(), tanggal_mulai_sewa, selesai_sewa, sisi)
            .enqueue(object : Callback<WrappedResponse<Order>>{
                override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                    result(false, Error(t.message))
                }

                override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                    if (response.isSuccessful){
                        val body  = response.body()
                        if (body?.status!!){
                            result(true, null)
                        }else{
                            result(false, Error(body.message))
                        }
                    }else{
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