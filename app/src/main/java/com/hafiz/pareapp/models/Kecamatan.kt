package com.hafiz.pareapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kecamatan(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("kecamatan") var kecamatan : String? = null
) : Parcelable