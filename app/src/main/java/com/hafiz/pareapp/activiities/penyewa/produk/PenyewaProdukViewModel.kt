package com.hafiz.pareapp.activiities.penyewa.produk

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.repositories.ProdukRepository
import com.hafiz.pareapp.utils.SingleLiveEvent

class PenyewaProdukViewModel (private val produkRepository: ProdukRepository) : ViewModel(){

    private val state : SingleLiveEvent<PenyewaProdukState> = SingleLiveEvent()
    private val produks = MutableLiveData<List<Produk>>()

    private fun setLoading() { state.value = PenyewaProdukState.IsLoading(true) }
    private fun hideLoading() { state.value = PenyewaProdukState.IsLoading(false) }
    private fun toast(message: String) { state.value = PenyewaProdukState.ShowToast(message) }

    fun searchProduk(token : String, tanggalMulai : String, lamaSewa: String){
        setLoading()
        produkRepository.searchProduk(token, tanggalMulai, lamaSewa){listProduk, error->
            hideLoading()
            error?.let { it.message?.let { m -> toast(m) } }
            listProduk?.let { produks.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToProduks() = produks
}
sealed class PenyewaProdukState{
    data class IsLoading(var state : Boolean = false) : PenyewaProdukState()
    data class ShowToast(var message : String) : PenyewaProdukState()
}