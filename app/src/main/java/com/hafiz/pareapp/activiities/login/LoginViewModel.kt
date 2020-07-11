package com.hafiz.pareapp.activiities.login

import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.utils.SingleResponse
import java.lang.Error

class LoginViewModel (private val userRepository: UserRepository) : ViewModel(){
    private var state : SingleLiveEvent<LoginState> = SingleLiveEvent()
    
    private fun toast(message: String) { state.value = LoginState.ShowToast(message) }
    private fun setLoading() { state.value = LoginState.IsLoading(true) }
    private fun hideLoading() { state.value = LoginState.IsLoading(false) }
    private fun reset() { state.value = LoginState.Reset }
    private fun successpemilik(token: String, r : Boolean) { state.value = LoginState.SuccessPemilik(token, r) }
    private fun successpenyewa(token: String, r : Boolean) { state.value = LoginState.SuccessPenyewa(token, r) }

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

    fun login(email: String, password: String, role: Int){
        setLoading()
        userRepository.login(email, password, role, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let {
                    val r = it.role
                    if(r) successpemilik(it.token.toString(), r) else successpenyewa(it.token.toString(), r)
                }
            }
            override fun onFailure(err: Error) {
                hideLoading()
                err.message?.let { toast(it) }
            }
        })
    }

    fun listenToState() = state
    
}
sealed class LoginState {
    object Reset : LoginState()
    data class IsLoading(var state: Boolean = false) : LoginState()
    data class role(var role : Boolean = true) : LoginState()
    data class ShowToast(var message : String) : LoginState()
    data class ShowAlert(var message : String) : LoginState()
    data class SuccessPemilik(var token : String, val role : Boolean) : LoginState()
    data class SuccessPenyewa(var token : String, val role : Boolean) : LoginState()
    data class Validate(var email : String? = null, var password : String? = null) : LoginState()
}