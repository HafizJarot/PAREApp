package com.hafiz.pareapp.ui.penyewa.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.SingleLiveEvent

class PenyewaProfileViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val currentUser = MutableLiveData<User>()
    private val state : SingleLiveEvent<PenyewaProfileState> = SingleLiveEvent()
    private fun setLoading(){ state.value = PenyewaProfileState.Loading(true) }
    private fun hideLoading(){ state.value = PenyewaProfileState.Loading(false) }
    private fun toast(message: String){ state.value = PenyewaProfileState.ShowToast(message) }

//    fun currentUser(token : String){
//        setLoading()
//        userRepository.profile(token){user, error ->
//            hideLoading()
//            error?.let { it.message?.let { message-> toast(message) } }
//            user?.let { currentUser.postValue(it) }
//        }
//    }



    fun listenToState() = state
    fun listenToCurrentUser() = currentUser
}

sealed class PenyewaProfileState{
    data class Loading(var state : Boolean = false) : PenyewaProfileState()
    data class ShowToast(var message : String) : PenyewaProfileState()
}