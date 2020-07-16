package com.hafiz.pareapp.repositories

import com.google.firebase.iid.FirebaseInstanceId
import com.hafiz.pareapp.utils.SingleResponse
import com.hafiz.pareapp.webservices.ApiService

interface FirebaseContract {
    fun generateFcmToken(listener : SingleResponse<String>)
}

class FirebaseRepository : FirebaseContract {
    override fun generateFcmToken(listener: SingleResponse<String>) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    it.result?.let {result->
                        listener.onSuccess(result.token)
                    } ?: kotlin.run {
                        listener.onFailure(Error("Failed to get firebase token"))
                    }
                }
                !it.isSuccessful -> listener.onFailure(Error("Cannot get firebase token"))
            }
        }
    }


}