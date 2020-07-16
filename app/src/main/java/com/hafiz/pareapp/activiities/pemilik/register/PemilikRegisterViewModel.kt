package com.hafiz.pareapp.activiities.pemilik.register

import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.RegisterPemilik
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.repositories.FirebaseRepository
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.utils.SingleResponse
import java.lang.Error

class PemilikRegisterViewModel (
    private val userRepository: UserRepository,
    private val firebaseRepository: FirebaseRepository) : ViewModel(){
    private val state : SingleLiveEvent<PemilikRegisterState> = SingleLiveEvent()
    private fun setLoading() { state.value = PemilikRegisterState.IsLoading(true) }
    private fun hideLoading() { state.value = PemilikRegisterState.IsLoading(false) }
    private fun toast(mesagge: String) { state.value = PemilikRegisterState.ShowToast(mesagge) }
    private fun success(email: String) { state.value = PemilikRegisterState.Success(email) }
    private fun reset() { state.value = PemilikRegisterState.Reset }

    fun validate(noIzin: String, namaPerusahaan: String, email: String, password: String, confirmPassword: String,
                 noHp: String, alamat : String) : Boolean {
        reset()

        if (noIzin.isEmpty()){
            state.value = PemilikRegisterState.Validate(noIzin = "no izin tidak boleh kosong")
            return false
        }

        if (namaPerusahaan.isEmpty()){
            state.value = PemilikRegisterState.Validate(namaPerusahaan = "nama perusahaan tidak boleh kosong")
            return false
        }

        if (namaPerusahaan.length < 5){
            state.value = PemilikRegisterState.Validate(namaPerusahaan = "nama perusahaan setidaknya 5 karakter")
            return false
        }

        if (email.isEmpty()){
            state.value = PemilikRegisterState.Validate(email = "email tidak boleh kosong")
            return false
        }

        if (!PareUtils.isValidEmail(email)) {
            state.value = PemilikRegisterState.Validate(email = "email tidak valid")
            return false
        }

        if (password.isEmpty()){
            state.value = PemilikRegisterState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!PareUtils.isValidPassword(password)){
            state.value = PemilikRegisterState.Validate(password = "password tidak valid")
            return false
        }

        if (confirmPassword.isEmpty()){
            state.value = PemilikRegisterState.Validate(confirmPassword = "konfirmasi password tidak boleh kosong")
            return false
        }

        if(!confirmPassword.equals(password)){
            state.value = PemilikRegisterState.Validate(confirmPassword = "konfirmasi password tidak cocok")
            return false
        }

        if (noHp.isEmpty()){
            state.value = PemilikRegisterState.Validate(noHp = "no telepon harus di isi")
            return false
        }

        if (noHp.length < 11 || noHp.length > 13){
            state.value = PemilikRegisterState.Validate(noHp = "no telepon setidaknya 11 sampai 13 karakter")
            return false
        }

        if (alamat.isEmpty()){
            state.value = PemilikRegisterState.Validate(alamat = "alamat tidak boleh kosong")
            return false
        }

        if (alamat.length < 10){
            state.value = PemilikRegisterState.Validate(alamat = "alamat minimal 10 karakter")
            return false
        }

        return true
    }

    fun register(user: RegisterPemilik){
        setLoading()
        generateTokenFirebase(user)
//        userRepository.registerPemilik(user){resultUser, error ->
//
//            error?.let { it.message?.let { message-> toast(message) } }
//            resultUser?.let { success(it.email!!) }
//        }
    }

    fun generateTokenFirebase(user: RegisterPemilik){
        setLoading()
        firebaseRepository.generateFcmToken(object : SingleResponse<String>{
            override fun onSuccess(data: String?) {
                hideLoading()
                data?.let { token ->
                    userRepository.registerPemilik(user, token){resultUser, error ->
                        error?.let { it.message?.let { message->
                            toast(message) }
                        }
                        resultUser?.let {
                            success(it.email!!)
                        }
                    }
                }

            }
            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })

    }

    fun listenToState() = state
}

sealed class PemilikRegisterState{
    data class IsLoading(var state : Boolean = false) : PemilikRegisterState()
    data class ShowToast(var mesagge : String) : PemilikRegisterState()
    data class Success(var email: String) : PemilikRegisterState()
    object Reset : PemilikRegisterState()
    data class Validate(
        var noIzin : String? = null,
        var namaPerusahaan : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPassword : String? = null,
        var noHp : String? = null,
        var alamat : String? = null
    ) : PemilikRegisterState()
}