package com.hafiz.pareapp.activiities.penyewa.update_profile

import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.utils.SingleResponse

class UpdateProfilViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val state: SingleLiveEvent<PenyewaUpdateProfilState> = SingleLiveEvent()
    private fun setLoading() { state.value = PenyewaUpdateProfilState.Loading(true) }
    private fun hideLoading() { state.value = PenyewaUpdateProfilState.Loading(false) }
    private fun toast(message: String) { state.value = PenyewaUpdateProfilState.ShowToast(message) }
    private fun success() { state.value = PenyewaUpdateProfilState.Success }

    fun updateProfile(token: String, name : String, pass : String, pathImage: String) {
        setLoading()
        if (name.isEmpty()){
            updatePhotoProfile(token, pathImage)
        }else {
            userRepository.updateProfil(token, name, pass, object : SingleResponse<User> {

                override fun onSuccess(data: User?) {
                    hideLoading()
                    if (pathImage.isNotEmpty()) {
                        updatePhotoProfile(token, pathImage)
                    } else {
                        success()
                    }
                }

                override fun onFailure(err: Error) {
                    toast(err.message.toString())
                }
            })
        }
    }

    private fun updatePhotoProfile(token: String, pathImage: String) {
        userRepository.updatePhotoProfil(token, pathImage, object : SingleResponse<User> {
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class PenyewaUpdateProfilState {
    data class Loading(var state: Boolean = false) : PenyewaUpdateProfilState()
    data class ShowToast(var message: String) : PenyewaUpdateProfilState()
    object Success : PenyewaUpdateProfilState()
    data class Validate(
        var name : String? = null,
        var pass: String? = null
    ) : PenyewaUpdateProfilState()
    object Reset : PenyewaUpdateProfilState()
}