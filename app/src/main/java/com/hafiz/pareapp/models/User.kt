package com.hafiz.pareapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("no_izin") var no_izin : String? = null,
    @SerializedName("nama_perusahaan") var nama_perusahaan : String? = null,
    @SerializedName("nama") var name : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("api_token") var token : String? = null,
    @SerializedName("active") var active : Boolean? = null,
    @SerializedName("alamat") var alamat : String? = null,
    @SerializedName("no_hp") var no_hp : String? = null,
    @SerializedName("role") var role : Boolean = false
) : Parcelable

data class RegisterPemilik(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("no_izin") var no_izin : String? = null,
    @SerializedName("nama_perusahaan") var nama_perusahaan : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("password") var password : String? = null,
    @SerializedName("alamat") var alamat : String? = null,
    @SerializedName("no_hp") var no_hp : String? = null
)

data class RegisterPenyewa(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama") var name : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("password") var password : String? = null
)