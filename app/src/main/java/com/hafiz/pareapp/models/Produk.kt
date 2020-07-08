package com.hafiz.pareapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Produk(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("panjang") var panjang: Int? = null,
    @SerializedName("lebar") var lebar: Int? = null,
    @SerializedName("sisi") var sisi: Int? = null,
    @SerializedName("foto") var foto: String? = null,
    @SerializedName("masa_berdiri") var masa_berdiri: String? = null,
    @SerializedName("keterangan") var keterangan: String? = null,
    @SerializedName("harga_sewa") var harga_sewa: Int? = null,
    @SerializedName("alamat") var alamat: String? = null,
    @SerializedName("status") var status: Boolean = false,
    @SerializedName("user") var user : User? = null
) : Parcelable