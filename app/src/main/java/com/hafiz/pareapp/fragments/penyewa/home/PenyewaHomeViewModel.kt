package com.hafiz.pareapp.fragments.penyewa.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.repositories.ProdukRepository
import com.hafiz.pareapp.utils.SingleLiveEvent

class PenyewaHomeViewModel (private val produkRepository: ProdukRepository) : ViewModel(){

    private val state : SingleLiveEvent<PenyewaHomeState> = SingleLiveEvent()
    private val produks = MutableLiveData<List<Produk>>()

    private fun setLoading() { state.value = PenyewaHomeState.IsLoading(true) }
    private fun hideLoading() { state.value = PenyewaHomeState.IsLoading(false) }
    private fun toast(message: String) { state.value = PenyewaHomeState.ShowToast(message) }

    fun getProduksPenyewa(token : String){
        setLoading()
        produkRepository.getProduks(token){listProduk, error->
            hideLoading()
            error?.let { it.message?.let { m -> toast(m) } }
            listProduk?.let { produks.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToProduks() = produks
}
sealed class PenyewaHomeState{
    data class IsLoading(var state : Boolean = false) : PenyewaHomeState()
    data class ShowToast(var message : String) : PenyewaHomeState()
}