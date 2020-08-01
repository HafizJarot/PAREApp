package com.hafiz.pareapp.repositories

import com.google.gson.GsonBuilder
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.utils.SingleResponse
import com.hafiz.pareapp.webservices.ApiService
import com.hafiz.pareapp.webservices.WrappedListResponse
import com.hafiz.pareapp.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface ProdukContract{
    fun updateProduk(token: String, id : String, produk: Produk, listemer : SingleResponse<Produk>)
    fun updateProdukPhoto(token: String, id : String, urlFoto: String, listemer : SingleResponse<Produk>)
    fun deleteProduk(token: String, id : String, listemer: SingleResponse<Produk>)
}

class ProdukRepository (private val api : ApiService) : ProdukContract {
    override fun deleteProduk(token: String, id: String, listemer: SingleResponse<Produk>) {
        api.deleteProduk(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Produk>>{
            override fun onFailure(call: Call<WrappedResponse<Produk>>, t: Throwable) {
                listemer.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Produk>>, response: Response<WrappedResponse<Produk>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listemer.onSuccess(body.data)
                        }else{
                            listemer.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listemer.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updateProduk( token: String, id: String, produk: Produk, listemer: SingleResponse<Produk>) {
        produk.user =null
        val g = GsonBuilder().create()
        val json = g.toJson(produk)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        println(json)
        api.updateProduk(token, id.toInt(), body).enqueue(object : Callback<WrappedResponse<Produk>>{
            override fun onFailure(call: Call<WrappedResponse<Produk>>, t: Throwable) {
                listemer.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Produk>>, response: Response<WrappedResponse<Produk>>) {
                println(response.body())
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!){
                            listemer.onSuccess(b.data)
                        }else{
                            println(response.body())
                            listemer.onFailure(Error(b.message))
                        }
                    }
                    !response.isSuccessful -> listemer.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updateProdukPhoto(token: String, id: String, urlFoto: String, listemer: SingleResponse<Produk>) {
        val file = File(urlFoto)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("foto", file.name, requestBodyForFile)
        api.updateProdukPhoto(token, id, image).enqueue(object : Callback<WrappedResponse<Produk>>{
            override fun onFailure(call: Call<WrappedResponse<Produk>>, t: Throwable) {
                listemer.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Produk>>, response: Response<WrappedResponse<Produk>>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listemer.onSuccess(body.data)
                        }else{
                            listemer.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listemer.onFailure(Error(response.message()))
                }
            }
        })
    }

    private fun createPartFromString(string: String) : RequestBody = RequestBody.create(MultipartBody.FORM, string)

    fun tambahProduk(token: String, produk: Produk, urlFoto: String, result: (Boolean, Error?) -> Unit){
        val map = HashMap<String, RequestBody>()
        map["masa_berdiri"] = createPartFromString(produk.masa_berdiri!!)
        map["keterangan"] = createPartFromString(produk.keterangan!!)
        map["harga_sewa"] = createPartFromString(produk.harga_sewa.toString())
        map["panjang"] = createPartFromString(produk.panjang.toString())
        map["lebar"] = createPartFromString(produk.lebar.toString())
        map["alamat"] = createPartFromString(produk.alamat!!)
        map["sisi"] = createPartFromString(produk.sisi.toString())
        val file = File(urlFoto)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("foto", file.name, requestBodyForFile)
        api.tambahproduk(token, map, image).enqueue(object : Callback<WrappedResponse<Produk>> {
            override fun onFailure(call: Call<WrappedResponse<Produk>>, t: Throwable) {
                result(false, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Produk>>, response: Response<WrappedResponse<Produk>>) {
                if (response.isSuccessful){
                    val body = response.body()
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



//    fun updateProduk(token: String, id : String, produk: Produk, urlFoto: String, result: (Boolean, Error?) -> Unit){
//        val map = HashMap<String, RequestBody>()
//        map["panjang"] = createPartFromString(produk.panjang.toString())
//        map["lebar"] = createPartFromString(produk.lebar.toString())
//        map["masa_berdiri"] = createPartFromString(produk.masa_berdiri!!)
//        map["keterangan"] = createPartFromString(produk.keterangan!!)
//        map["harga_sewa"] = createPartFromString(produk.harga_sewa.toString())
//        map["alamat"] = createPartFromString(produk.alamat!!)
//        val file = File(urlFoto)
//        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
//        val image = MultipartBody.Part.createFormData("foto", file.name, requestBodyForFile)
//        api.updateproduk(token, id.toInt(), map, image).enqueue(object : Callback<WrappedResponse<Produk>>{
//            override fun onFailure(call: Call<WrappedResponse<Produk>>, t: Throwable) {
//                result(false, Error(t.message))
//            }
//
//            override fun onResponse(call: Call<WrappedResponse<Produk>>, response: Response<WrappedResponse<Produk>>) {
//                if (response.isSuccessful){
//                    val body = response.body()
//                    if (body?.status!!){
//                        result(true, null)
//                    }else{
//                        result(false, Error(body.message))
//                    }
//                }else{
//                    result(false, Error(response.message()))
//                }
//            }
//
//        })
//    }

    fun getProduks(token : String, result : (List<Produk>?, Error?)-> Unit){
        api.getAllProduk(token).enqueue(object : Callback<WrappedListResponse<Produk>>{
            override fun onFailure(call: Call<WrappedListResponse<Produk>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Produk>>, response: Response<WrappedListResponse<Produk>>) {
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

    fun getProduksPemilik(token: String, result: (List<Produk>?, Error?) -> Unit){
        api.getProdukPemilik(token).enqueue(object : Callback<WrappedListResponse<Produk>>{
            override fun onFailure(call: Call<WrappedListResponse<Produk>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Produk>>, response: Response<WrappedListResponse<Produk>>) {
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

    fun searchProduk(token : String, tanggalMulai : String, lamaSewa : String, result : (List<Produk>?, Error?)-> Unit){
        println(tanggalMulai)
        println(lamaSewa)
        api.searchProduk(token, tanggalMulai, lamaSewa.toInt()).enqueue(object : Callback<WrappedListResponse<Produk>>{
            override fun onFailure(call: Call<WrappedListResponse<Produk>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Produk>>, response: Response<WrappedListResponse<Produk>>) {
                println("ydhnwb -> ${response.body()}")
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