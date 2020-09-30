package com.hafiz.pareapp.ui.penyewa.detail_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.repositories.OrderRepository
import com.hafiz.pareapp.utils.SingleLiveEvent
import java.time.Month
import java.util.*

class PenyewaDetailProdukViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<DetailProdukState> = SingleLiveEvent()
    private val startDate = MutableLiveData<String>()
    private val endDate = MutableLiveData<String>()
    private val diffMonth = MutableLiveData<Int>()

    private fun isLoading(b : Boolean){ state.value = DetailProdukState.Loading(b) }
    private fun toast(m : String){ state.value = DetailProdukState.ShowToast(m) }

    fun setStartDate(date : String){
        startDate.value = date
        calcDate()
    }

    fun setEndDate(date: String){
        endDate.value = date
    }

    private fun calcDate(){
        val yearsInBetween = endDate.value!![Calendar.YEAR] - startDate.value!![Calendar.YEAR]
        val monthsDiff = endDate.value!![Calendar.MONTH] - startDate.value!![Calendar.MONTH]
        diffMonth.value = yearsInBetween*12 + monthsDiff
    }

    fun listenToStartDate() = startDate
    fun listenToEndDate() = endDate
    fun listenToDiffMonth() = diffMonth

}

sealed class DetailProdukState{
    data class Loading(var state : Boolean = false) : DetailProdukState()
    data class ShowToast(var message : String) : DetailProdukState()
}