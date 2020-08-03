package com.hafiz.pareapp.fragments.pemilik.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.utils.SingleResponse
import java.lang.Error

class PemilikProfileViewModel(private val userRepository: UserRepository) : ViewModel(){
    private val currentUser = MutableLiveData<User>()
    private val state : SingleLiveEvent<PemilikProfileState> = SingleLiveEvent()
    private fun setLoading(){ state.value = PemilikProfileState.Loading(true) }
    private fun hideLoading(){ state.value = PemilikProfileState.Loading(false) }
    private fun toast(message: String){ state.value = PemilikProfileState.ShowToast(message) }

    fun currentUser(token : String){
        setLoading()
        userRepository.profile(token){user, error ->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) } }
            user?.let { currentUser.postValue(it) }
        }
    }




    fun ambilUang(token: String, saldo : String, namaBank : String,
                  namaRekening : String, nomorRekening : String){
        setLoading()
        userRepository.ambilUang(token, saldo, namaBank, namaRekening, nomorRekening, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let {
                    state.value = PemilikProfileState.SuccessAmbilUang
                }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun listenToState() = state
    fun listenToCurrentUser() = currentUser
}

sealed class PemilikProfileState{
    data class Loading(var state : Boolean = false) : PemilikProfileState()
    data class ShowToast(var message : String) : PemilikProfileState()
    object SuccessAmbilUang : PemilikProfileState()
    object Reset : PemilikProfileState()
    data class Validate(
        var saldo: String? = null,
        var namaBank: String? = null,
        var namaRekening: String? = null,
        var nomorRekening: String? = null
    ) : PemilikProfileState()
}