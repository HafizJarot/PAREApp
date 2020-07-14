package com.hafiz.pareapp.webservices

import com.google.gson.annotations.SerializedName
import com.hafiz.pareapp.models.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.HashMap
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        const val ENDPOINT = "https://papanreklame.herokuapp.com/"
        private var retrofit: Retrofit? = null
        private var opt = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }.build()

        private fun getClient(): Retrofit {
            return if (retrofit == null) {
                retrofit = Retrofit.Builder().apply {
                    baseUrl(ENDPOINT)
                    client(opt)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofit!!
            } else {
                retrofit!!
            }
        }

        fun instance() = getClient().create(ApiService::class.java)
    }
}

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/user/register/pemilik")
    fun regiserPemilik(
        @Body body: RequestBody
    ) : Call<WrappedResponse<RegisterPemilik>>

    @FormUrlEncoded
    @POST("api/user/register/penyewa")
    fun registerPenyewa(
        @Field("nama") nama : String,
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("alamat") alamat : String
    ) : Call<WrappedResponse<RegisterPenyewa>>

    @FormUrlEncoded
    @POST("api/user/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: Int
    ): Call<WrappedResponse<User>>

    @GET("api/produk/all")
    fun getAllProduk(@Header("Authorization") token: String): Call<WrappedListResponse<Produk>>

    @FormUrlEncoded
    @POST("api/produk/search")
    fun searchProduk(
        @Header("Authorization") token: String,
        @Field("tanggal_mulai") tanggalMulaiSewa: String,
        @Field("tanggal_selesai") lamaSewa: Int
    ): Call<WrappedListResponse<Produk>>

    @GET("api/user/profile")
    fun profile(@Header("Authorization") token: String): Call<WrappedResponse<User>>

    @GET("api/produk")
    fun getProdukPemilik(@Header("Authorization") token: String): Call<WrappedListResponse<Produk>>

    @Multipart
    @POST("api/produk/store")
    fun tambahproduk(
        @Header("Authorization") token: String,
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ): Call<WrappedResponse<Produk>>

    @Multipart
    @POST("api/produk/{id}/update")
    fun updateproduk(
        @Header("Authorization") token: String,
        @Path("id") id: Int? = null,
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ): Call<WrappedResponse<Produk>>

//    @FormUrlEncoded
//    @POST("api/order/store")
//    fun orderStore(
//        @Header("Authorization") token: String,
//        @Field("id_pemilik") id_pemilik: Int,
//        @Field("id_produk") id_produk: Int,
//        @Field("harga") harga: Int,
//        @Field("tanggal_mulai_sewa") tanggal_mulai_sewa: String,
//        @Field("selesai_sewa") selesai_sewa: String,
//        @Field("sisi") sisi: String
//    ): Call<WrappedResponse<Order>>

    @Headers("Content-Type: application/json")
    @POST("api/order/store")
    fun createOrder(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ) : Call<WrappedResponse<CreateOrder>>

    @GET("api/order/penyewa")
    fun getPenyewaMyOrders(
        @Header("Authorization") token: String
    ): Call<WrappedListResponse<Order>>

    @GET("api/order/pemilik")
    fun getPemilikMyOrders(
        @Header("Authorization") token: String
    ): Call<WrappedListResponse<Order>>

}


data class WrappedResponse<T>(
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Boolean,
    @SerializedName("data") var data: T
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Boolean,
    @SerializedName("data") var data: List<T>
)