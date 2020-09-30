package com.hafiz.pareapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Penyewa(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama") var nama : String? = null,
    @SerializedName("alamat") var alamat: String? = null
) : Parcelable