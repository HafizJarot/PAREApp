package com.hafiz.pareapp.repositories

import com.hafiz.pareapp.models.Kecamatan
import com.hafiz.pareapp.utils.ArrayResponse
import com.hafiz.pareapp.webservices.ApiService
import com.hafiz.pareapp.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface KecamatanContract{
    fun fetchKecamaatan(listener : ArrayResponse<Kecamatan>)
}

class KecamatanRepository (private val api: ApiService) : KecamatanContract {
    override fun fetchKecamaatan(listener: ArrayResponse<Kecamatan>) {
        api.kecamatan().enqueue(object : Callback<WrappedListResponse<Kecamatan>>{
            override fun onFailure(call: Call<WrappedListResponse<Kecamatan>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Kecamatan>>,
                response: Response<WrappedListResponse<Kecamatan>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}