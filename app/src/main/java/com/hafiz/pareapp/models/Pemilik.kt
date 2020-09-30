package com.hafiz.pareapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pemilik(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("no_izin") var no_izin: String? = null,
    @SerializedName("nama_perusahaan") var nama_perusahaan : String? = null,
    @SerializedName("alamat") var alamat : String? = null
) : Parcelable