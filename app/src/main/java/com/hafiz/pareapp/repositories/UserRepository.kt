package com.hafiz.pareapp.repositories

import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.utils.SingleResponse
import com.hafiz.pareapp.webservices.ApiService
import com.hafiz.pareapp.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface UserContract {
    fun login(email: String, passsword: String, role: Int, listener: SingleResponse<User>)
}

class UserRepository (private val api : ApiService) : UserContract{
    override fun login(email: String, passsword: String, role: Int, listener: SingleResponse<User>) {
        api.login(email, passsword, role).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b!!.status) listener.onSuccess(b.data) else listener.onFailure(Error(b.message))
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }


    fun profile(token : String, result: (User?, Error?) -> Unit){
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error(body.message))
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }

        })
    }
}