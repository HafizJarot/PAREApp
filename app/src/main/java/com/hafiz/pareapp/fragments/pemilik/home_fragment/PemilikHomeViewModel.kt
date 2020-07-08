package com.hafiz.pareapp.fragments.pemilik.home_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.repositories.ProdukRepository
import com.hafiz.pareapp.utils.SingleLiveEvent

class PemilikHomeViewModel (private val produkRepository: ProdukRepository) : ViewModel(){
    private val state : SingleLiveEvent<PemilikHomeState> = SingleLiveEvent()
    private val produks = MutableLiveData<List<Produk>>()

    private fun toast(message: String) { state.value = PemilikHomeState.ShowToast(message) }
    private fun setLoading() { state.value = PemilikHomeState.IsLoading(true) }
    private fun hideLoading() { state.value = PemilikHomeState.IsLoading(false) }
    private fun success() { state.value = PemilikHomeState.Success }

    fun getMyProduks(token : String){
        setLoading()
        produkRepository.getProduksPemilik(token){listProduk, error->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) }}
            listProduk?.let { produks.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToProduks() = produks
}
sealed class PemilikHomeState{
    data class IsLoading(var state : Boolean = false) : PemilikHomeState()
    data class ShowToast(var message : String) : PemilikHomeState()
    object Success : PemilikHomeState()
    object Reset : PemilikHomeState()
}