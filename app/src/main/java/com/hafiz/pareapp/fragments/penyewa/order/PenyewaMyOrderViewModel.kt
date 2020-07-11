package com.hafiz.pareapp.fragments.penyewa.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Order
import com.hafiz.pareapp.repositories.OrderRepository
import com.hafiz.pareapp.utils.SingleLiveEvent

class PenyewaMyOrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    private val state : SingleLiveEvent<PenyewaMyOrderSate> = SingleLiveEvent()
    private val orders = MutableLiveData<List<Order>>()

    private fun setLoading() { state.value = PenyewaMyOrderSate.IsLoading(true) }
    private fun hideLoading() { state.value = PenyewaMyOrderSate.IsLoading(false) }
    private fun toast(message: String) { state.value = PenyewaMyOrderSate.ShwoToast(message) }


    fun getPenyewaMyOrders(token : String){
        setLoading()
        orderRepository.getPenyewaMyOrders(token){listOrder, error->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) } }
            listOrder?.let { orders.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToOrders() = orders
}

sealed class PenyewaMyOrderSate{
    data class IsLoading(var state : Boolean = false) : PenyewaMyOrderSate()
    data class ShwoToast(var message : String) : PenyewaMyOrderSate()
}