package com.hafiz.pareapp.activiities.penyewa.register

import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.repositories.OrderRepository
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.SingleLiveEvent

class PenyewaRegisterViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<PenyewaRegisterState> = SingleLiveEvent()

    private fun setLoading(){ state.value = PenyewaRegisterState.IsLoading(true) }
    private fun hideLoading(){ state.value = PenyewaRegisterState.IsLoading(false) }
    private fun toast(message: String) { state.value = PenyewaRegisterState.ShowToast(message) }
    private fun success(email: String) { state.value = PenyewaRegisterState.Success(email) }

    fun validate(name: String, email: String, password: String, confirmPassword : String, alamat : String): Boolean{
        state.value = PenyewaRegisterState.Reset

        if (name.isEmpty()){
            state.value = PenyewaRegisterState.Validate(name = "nama tidak boleh kosong")
            return false
        }

        if (name.length < 5){
            state.value = PenyewaRegisterState.Validate(name= "nama setidaknya 5 karakter")
            return false
        }

        if (email.isEmpty()){
            state.value = PenyewaRegisterState.Validate(email = "email tidak boleh kosong")
            return false
        }

        if (!PareUtils.isValidEmail(email)){
            state.value = PenyewaRegisterState.Validate(email = "email tidak valid")
            return false
        }

        if (password.isEmpty()){
            state.value = PenyewaRegisterState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!PareUtils.isValidPassword(password)){
            state.value = PenyewaRegisterState.Validate(password = "password minimal 8 karakter")
            return false
        }

        if (confirmPassword.isEmpty()){
            state.value = PenyewaRegisterState.Validate(confirmPassword = "konfirmasi password tidak boleh kosong")
            return false
        }

        if(!confirmPassword.equals(password)){
            state.value = PenyewaRegisterState.Validate(confirmPassword = "konfirmasi password tidak cocok")
            return false
        }

        if (alamat.isEmpty()){
            state.value = PenyewaRegisterState.Validate(alamat = "alamat tidak boleh kosong")
            return false
        }
        return true
    }


    fun register(nama : String, email: String, pass : String,alamat: String){
        setLoading()
        userRepository.registerPenyewa(nama, email, pass, alamat){resultRegister, error ->
            hideLoading()
            error?.let { it.message?.let { message -> toast(message) } }
            resultRegister?.let {
                success(it.email!!)
            }
        }
    }

    fun listenToState() = state
}

sealed class PenyewaRegisterState {
    data class IsLoading(var state : Boolean = false) : PenyewaRegisterState()
    data class ShowToast(var message : String) : PenyewaRegisterState()
    data class Success(var email : String) : PenyewaRegisterState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPassword : String? = null,
        var alamat : String? = null
    ) : PenyewaRegisterState()
    object Reset : PenyewaRegisterState()
}