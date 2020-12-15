package com.hafiz.pareapp.repositories

import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.utils.ArrayResponse
import com.hafiz.pareapp.webservices.ApiService
import com.hafiz.pareapp.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface CompanyInterface{
    fun fetchCompanies(token : String, listener : ArrayResponse<Pemilik>)
    fun searchCompanies(token: String, id_kecamatan : String, listener: ArrayResponse<Pemilik>)
}

class CompanyRepository (private val api : ApiService) : CompanyInterface{
    override fun searchCompanies(
        token: String,
        id_kecamatan: String,
        listener: ArrayResponse<Pemilik>
    ) {
        api.searchPemilik(token, id_kecamatan.toInt()).enqueue(object : Callback<WrappedListResponse<Pemilik>>{
            override fun onFailure(call: Call<WrappedListResponse<Pemilik>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Pemilik>>,
                response: Response<WrappedListResponse<Pemilik>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchCompanies(token: String, listener: ArrayResponse<Pemilik>) {
        api.fetchCompanies(token).enqueue(object : Callback<WrappedListResponse<Pemilik>>{
            override fun onFailure(call: Call<WrappedListResponse<Pemilik>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Pemilik>>, response: Response<WrappedListResponse<Pemilik>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

}