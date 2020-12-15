package com.hafiz.pareapp.ui.penyewa.detail_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.CreateOrder
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.repositories.OrderRepository
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.utils.SingleResponse
import com.hafiz.pareapp.webservices.WrappedResponse
import retrofit2.Callback
import java.lang.Error
import java.time.Month
import java.util.*

class PenyewaDetailProdukViewModel (private val orderRepository: OrderRepository,
                                    private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<DetailProdukState> = SingleLiveEvent()
    private val currentuser = MutableLiveData<User>()

    private fun isLoading(b : Boolean){ state.value = DetailProdukState.Loading(b) }
    private fun toast(m : String){ state.value = DetailProdukState.ShowToast(m) }

    fun currentUser(token : String){
        isLoading(true)
        userRepository.profile(token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { currentuser.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun orderStore(token : String, createOrder: CreateOrder){
        isLoading(true)
        orderRepository.orderStore(token, createOrder){resultBool, error->
            isLoading(false)
            error?.let { it.message?.let { message-> toast(message) } }
            if (resultBool){
                state.value = DetailProdukState.Success
            }
        }
    }

    fun listenToCurrentuser() = currentuser
    fun listenToState() = state


}

sealed class DetailProdukState{
    data class Loading(var state : Boolean = false) : DetailProdukState()
    data class ShowToast(var message : String) : DetailProdukState()
    object Success : DetailProdukState()
}