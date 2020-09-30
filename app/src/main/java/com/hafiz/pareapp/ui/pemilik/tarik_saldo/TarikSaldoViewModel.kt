package com.hafiz.pareapp.ui.pemilik.tarik_saldo

import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.utils.SingleResponse
import java.lang.Error

class TarikSaldoViewModel(private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<TarikSaldoState> = SingleLiveEvent()
    private fun setLoading(){ state.value = TarikSaldoState.Loading(true) }
    private fun hideLoading(){ state.value = TarikSaldoState.Loading(false) }
    private fun toast(message: String){ state.value = TarikSaldoState.ShowToast(message) }

    fun validate(saldo: String, namaBank: String, namaRekening: String, nomorRekening: String) : Boolean {
        state.value = TarikSaldoState.Reset
        if (saldo.isEmpty()){
            state.value = TarikSaldoState.Validate(saldo = "saldo tidak boleh kosong")
            return false
        }

        if (namaBank.isEmpty()){
            state.value = TarikSaldoState.Validate(namaBank = "nama bank tidak boleh kosong")
            return false
        }

        if (namaRekening.isEmpty()){
            state.value = TarikSaldoState.Validate(namaRekening = "nama rekening tidak boleh kosong")
            return false
        }

        if (nomorRekening.isEmpty()){
            state.value = TarikSaldoState.Validate(nomorRekening = "nomor rekening tidak boleh kosong")
            return false
        }
        return true
    }


    fun ambilUang(token: String, saldo : String, namaBank : String,
                  namaRekening : String, nomorRekening : String){
        setLoading()
        userRepository.ambilUang(token, saldo, namaBank, namaRekening, nomorRekening, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let {
                    state.value = TarikSaldoState.SuccessAmbilUang
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

sealed class TarikSaldoState{
    data class Loading(var state : Boolean = false) : TarikSaldoState()
    data class ShowToast(var message : String) : TarikSaldoState()
    object SuccessAmbilUang : TarikSaldoState()
    object Reset : TarikSaldoState()
    data class Validate(
        var saldo: String? = null,
        var namaBank: String? = null,
        var namaRekening: String? = null,
        var nomorRekening: String? = null
    ) : TarikSaldoState()
}