package com.hafiz.pareapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("harga") var harga: Int? = null,
    @SerializedName("total_harga") var total_harga: Int? = null,
    @SerializedName("tanggal_mulai_sewa") var tanggal_mulai_sewa: String? = null,
    @SerializedName("selesai_sewa") var selesai_sewa: String? = null,
    @SerializedName("verifikasi") var verifikasi: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("snap") var snap: String? = null,
    @SerializedName("penyewa") var penyewa: User,
    @SerializedName("pemilik") var pemilik: User,
    @SerializedName("produk") var produk: Produk
) : Parcelable

data class CreateOrder(
    @SerializedName("id_pemilik") var idPemilik : String,
    @SerializedName("id_produk") var idProduk : String,
    @SerializedName("harga") var harga: String,
    @SerializedName("tanggal_mulai_sewa") var tanggalMulaiSewa: String,
    @SerializedName("selesai_sewa") var selesaiSewa : String,
    @SerializedName("sisi") var sisi : String,
    @SerializedName("status") var status : String? = null
)