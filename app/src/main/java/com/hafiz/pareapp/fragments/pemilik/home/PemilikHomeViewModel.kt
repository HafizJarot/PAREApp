package com.hafiz.pareapp.fragments.pemilik.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.repositories.ProdukRepository
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.utils.SingleResponse
import java.lang.Error

class PemilikHomeViewModel (private val produkRepository: ProdukRepository) : ViewModel(){
    private val state : SingleLiveEvent<PemilikHomeState> = SingleLiveEvent()
    private val produks = MutableLiveData<List<Produk>>()

    private fun toast(message: String) { state.value = PemilikHomeState.ShowToast(message) }
    private fun setLoading() { state.value = PemilikHomeState.IsLoading(true) }
    private fun hideLoading() { state.value = PemilikHomeState.IsLoading(false) }
    private fun success() { state.value = PemilikHomeState.Success }
    private fun successDelete() { state.value = PemilikHomeState.SuccessDelete }

    fun getMyProduks(token : String){
        setLoading()
        produkRepository.getProduksPemilik(token){listProduk, error->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) }}
            listProduk?.let { produks.postValue(it) }
        }
    }


    fun deleteProduk(token: String, id : String){
        setLoading()
        produkRepository.deleteProduk(token, id, object : SingleResponse<Produk> {
            override fun onSuccess(data: Produk?) {
                hideLoading()
                data?.let { successDelete() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun listenToState() = state
    fun listenToProduks() = produks
}
sealed class PemilikHomeState{
    data class IsLoading(var state : Boolean = false) : PemilikHomeState()
    data class ShowToast(var message : String) : PemilikHomeState()
    object Success : PemilikHomeState()
    object Reset : PemilikHomeState()
    object SuccessDelete : PemilikHomeState()
}