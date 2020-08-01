package com.hafiz.pareapp.repositories

import com.google.gson.GsonBuilder
import com.hafiz.pareapp.models.RegisterPemilik
import com.hafiz.pareapp.models.RegisterPenyewa
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.utils.SingleResponse
import com.hafiz.pareapp.webservices.ApiService
import com.hafiz.pareapp.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface UserContract {
    fun profile(token: String, listener : SingleResponse<User>)
    fun login(email: String, passsword: String, role: Int, listener: SingleResponse<User>)
    fun updateProfil(token: String, name : String, password: String, listener: SingleResponse<User>)
    fun updatePhotoProfil(token: String, imgUrl : String, listener: SingleResponse<User>)
}

class UserRepository (private val api : ApiService) : UserContract{

    override fun updatePhotoProfil(token: String, imgUrl: String, listener: SingleResponse<User>) {
        val file = File(imgUrl)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("avatar", file.name, requestBodyForFile)

        api.updatePhotoProfile(token, image).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updateProfil(token: String, name: String, password: String, listener: SingleResponse<User>) {
        api.updateProfile(token, name, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun profile(token: String, listener: SingleResponse<User>) {
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }


    override fun login(email: String, passsword: String, role: Int, listener: SingleResponse<User>) {
        api.login(email, passsword, role).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b!!.status) listener.onSuccess(b.data)
                        else listener.onFailure(Error(b.message))
                    }
                    else -> listener.onFailure(Error("Masukkan email dan password yang benar"))
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

    fun registerPemilik(user : RegisterPemilik, fcmToken : String, result: (RegisterPemilik?, Error?) -> Unit){
        val g = GsonBuilder().create()
        user.fcm_token = fcmToken
        val json = g.toJson(user)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.regiserPemilik(body).enqueue(object : Callback<WrappedResponse<RegisterPemilik>>{
            override fun onFailure(call: Call<WrappedResponse<RegisterPemilik>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<RegisterPemilik>>, response: Response<WrappedResponse<RegisterPemilik>>) {
                if (response.isSuccessful){
                    val b = response.body()
                    if (b?.status!!){
                        val data = b.data
                        result(data, null)
                    }else{
                        result(null, Error("maaf, gagal register"))
                    }
                }else{
                    result(null, Error("gagal register"))
                }
            }
        })
    }

    fun registerPenyewa(nama : String, email: String, passsword: String, alamat : String, result: (RegisterPenyewa?, Error?) -> Unit){
        api.registerPenyewa(nama, email, passsword, alamat).enqueue(object : Callback<WrappedResponse<RegisterPenyewa>>{
            override fun onFailure(call: Call<WrappedResponse<RegisterPenyewa>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<RegisterPenyewa>>, response: Response<WrappedResponse<RegisterPenyewa>>) {
                if (response.isSuccessful){
                    val b = response.body()
                    if (b?.status!!){
                        val data = b.data
                        result(data, null)
                    }else{
                        result(null, Error("maaf, gagal register"))
                    }
                }else{
                    result(null, Error("gagal register"))
                }
            }

        })
    }
}