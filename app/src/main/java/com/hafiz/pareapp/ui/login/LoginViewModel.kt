package com.hafiz.pareapp.ui.login

import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.repositories.FirebaseRepository
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.utils.SingleResponse
import java.lang.Error

class LoginViewModel (private val userRepository: UserRepository,
                      private val firebaseRepository: FirebaseRepository) : ViewModel(){
    private var state : SingleLiveEvent<LoginState> = SingleLiveEvent()
    
    private fun toast(message: String) { state.value = LoginState.ShowToast(message) }
    private fun isLoading(b : Boolean) { state.value = LoginState.IsLoading(b) }
    private fun reset() { state.value = LoginState.Reset }
    private fun successPemilik(token: String, r : Boolean) { state.value = LoginState.SuccessPemilik(token, r) }
    private fun successPenyewa(token: String, r : Boolean) { state.value = LoginState.SuccessPenyewa(token, r) }

    fun validate(email: String, password : String) : Boolean{
        reset()
        if(!PareUtils.isValidEmail(email)){
            state.value = LoginState.Validate(email = "Email tidak valid")
            return false
        }
        if(!PareUtils.isValidPassword(password)){
            state.value = LoginState.Validate(password = "Password setidaknya delapan karakter")
            return false
        }
        return true
    }

    private fun generateTokenFirebase(email: String, password: String){
        firebaseRepository.generateFcmToken(object : SingleResponse<String>{
            override fun onSuccess(data: String?) {
                data?.let {
                    //toast(it)
                    doLogin(email, password, it)
                } ?: run{
                    toast("Cannot get fcm token")
                    isLoading(false)
                }
            }
            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })

    }

    private fun doLogin(email: String, password: String, token: String) {
        userRepository.login(email, password, token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let {
                    val r = it.role
                    if(r) successPemilik(it.token.toString(), r) else successPenyewa(it.token.toString(), r)
                }
            }
            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun login(email: String, password: String) {
        isLoading(true)
        generateTokenFirebase(email, password)
    }

    fun listenToState() = state
    
}
sealed class LoginState {
    object Reset : LoginState()
    data class IsLoading(var state: Boolean = false) : LoginState()
    data class ShowToast(var message : String) : LoginState()
    data class ShowAlert(var message : String) : LoginState()
    data class SuccessPemilik(var token : String, val role : Boolean) : LoginState()
    data class SuccessPenyewa(var token : String, val role : Boolean) : LoginState()
    data class Validate(var email : String? = null, var password : String? = null) : LoginState()
}