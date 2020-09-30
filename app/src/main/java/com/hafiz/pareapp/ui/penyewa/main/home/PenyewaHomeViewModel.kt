package com.hafiz.pareapp.ui.penyewa.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.repositories.CompanyRepository
import com.hafiz.pareapp.repositories.ProdukRepository
import com.hafiz.pareapp.utils.ArrayResponse
import com.hafiz.pareapp.utils.SingleLiveEvent
import java.lang.Error

class PenyewaHomeViewModel (private val companyRepository: CompanyRepository) : ViewModel(){

    private val state : SingleLiveEvent<PenyewaHomeState> = SingleLiveEvent()
    private val companies = MutableLiveData<List<Pemilik>>()

    private fun isLoading(b : Boolean) { state.value = PenyewaHomeState.Loading(b) }
    private fun toast(message: String) { state.value = PenyewaHomeState.ShowToast(message) }

    fun fetchCompanies(token : String) {
        isLoading(true)

        companyRepository.fetchCompanies(token, object : ArrayResponse<Pemilik>{
            override fun onSuccess(datas: List<Pemilik>?) {
                isLoading(false)
                datas?.let { companies.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToCompanies() = companies
}
sealed class PenyewaHomeState{
    data class Loading(var state : Boolean = false) : PenyewaHomeState()
    data class ShowToast(var message : String) : PenyewaHomeState()
}