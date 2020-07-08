package com.hafiz.pareapp.fragments.pemilik.order_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Order
import com.hafiz.pareapp.repositories.OrderRepository
import com.hafiz.pareapp.utils.SingleLiveEvent

class PemilikMyOrderViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<PemilikMyOrderState> = SingleLiveEvent()
    private val orders = MutableLiveData<List<Order>>()
    private fun setLoading() { state.value = PemilikMyOrderState.IsLoading(true) }
    private fun hideLoading() { state.value = PemilikMyOrderState.IsLoading(false) }
    private fun toast(message: String) { state.value = PemilikMyOrderState.ShowToast(message) }

    fun getMyOrders(token : String){
        setLoading()
        orderRepository.getPemilikMyOrders(token){listOrder, error ->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) } }
            listOrder?.let { orders.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToOrders() = orders

}

sealed class PemilikMyOrderState{
    data class IsLoading(var state : Boolean = false) : PemilikMyOrderState()
    data class ShowToast(var message : String) : PemilikMyOrderState()
}