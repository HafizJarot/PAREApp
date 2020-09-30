package com.hafiz.pareapp.ui.penyewa.detail_company

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.repositories.ProdukRepository
import com.hafiz.pareapp.utils.ArrayResponse
import com.hafiz.pareapp.utils.SingleLiveEvent
import java.lang.Error

class PenyewaProductViewModel (private val produkRepository: ProdukRepository) : ViewModel(){
    private val state : SingleLiveEvent<PenyewaProductState> = SingleLiveEvent()
    private val products = MutableLiveData<List<Produk>>()

    private fun isLoading(b : Boolean) { state.value = PenyewaProductState.Loading(b) }
    private fun toast(m : String){ state.value = PenyewaProductState.ShowToast(m) }

    fun fetchProductByCompany(token : String, id_pemilik : String){
        isLoading(true)
        produkRepository.fetchProductByCompany(token, id_pemilik, object : ArrayResponse<Produk>{
            override fun onSuccess(datas: List<Produk>?) {
                isLoading(false)
                datas?.let { products.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToProducts() = products


}

sealed class PenyewaProductState{
    data class Loading(var state : Boolean = false) : PenyewaProductState()
    data class ShowToast(var message : String) : PenyewaProductState()
}