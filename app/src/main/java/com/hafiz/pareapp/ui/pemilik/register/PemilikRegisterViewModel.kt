package com.hafiz.pareapp.ui.pemilik.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.models.RegisterPemilik
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
    private val pemilik = MutableLiveData<Pemilik>()
    private val noIzin = MutableLiveData<String>()


    private fun setLoading() { state.value = PemilikRegisterState.IsLoading(true) }
    private fun hideLoading() { state.value = PemilikRegisterState.IsLoading(false) }
    private fun toast(mesagge: String) { state.value = PemilikRegisterState.ShowToast(mesagge) }
    private fun success() { state.value = PemilikRegisterState.Success }
    private fun reset() { state.value = PemilikRegisterState.Reset }

    fun validateNoIzin(noIzin: String) : Boolean {
        if (noIzin.isEmpty()){
            state.value = PemilikRegisterState.Validate(noIzin = "no izin tidak boleh kosong")
            return false
        }

        return true
    }


    fun validate(noIzin: String, email: String, password: String, confirmPassword: String,
                 noHp: String) : Boolean {
        reset()

        if (noIzin.isEmpty()){
            state.value = PemilikRegisterState.Validate(noIzin = "no izin tidak boleh kosong")
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

        return true
    }

    fun register(user: RegisterPemilik){
        setLoading()
        generateTokenFirebase(user)
    }

    private fun generateTokenFirebase(user: RegisterPemilik){
        setLoading()
        firebaseRepository.generateFcmToken(object : SingleResponse<String>{
            override fun onSuccess(data: String?) {
                data?.let { token ->
                    userRepository.registerPemilik(user, token, object : SingleResponse<RegisterPemilik>{
                        override fun onSuccess(data: RegisterPemilik?) {
                            data?.let { success() }
                        }

                        override fun onFailure(err: Error) {
                            toast(err.message.toString())
                        }
                    })

                } ?: run{
                    toast("Cannot get fcm token")
                    hideLoading()
                }
            }
            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })

    }

    fun checkNoIzin(noIzin: String){
        setLoading()
        userRepository.checkNoIzin(noIzin, object : SingleResponse<Pemilik>{
            override fun onSuccess(data: Pemilik?) {
                hideLoading()
                data?.let { pemilik.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast("no izin tidak di temukan")
            }

        })
    }

    fun setNoIzin(no: String) {
        noIzin.postValue(no)
    }

    fun getNoIzin() = noIzin
    fun listenToState() = state
    fun listenToPemilik() = pemilik
}

sealed class PemilikRegisterState{
    data class IsLoading(var state : Boolean = false) : PemilikRegisterState()
    data class ShowToast(var mesagge : String) : PemilikRegisterState()
    object Success : PemilikRegisterState()
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