package com.hafiz.pareapp.activiities.penyewa.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.repositories.OrderRepository
import com.hafiz.pareapp.repositories.UserRepository
import com.hafiz.pareapp.utils.SingleLiveEvent

class PenyewaOrderViewModel (private val userRepository: UserRepository, private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<PenyewaOrderState> = SingleLiveEvent()
    private val user = MutableLiveData<User>()

    private fun toast(message: String) { state.value = PenyewaOrderState.ShowToast(message) }
    private fun setLoading() { state.value = PenyewaOrderState.IsLoading(true) }
    private fun hideLoading() { state.value = PenyewaOrderState.IsLoading(false) }
    private fun success() { state.value = PenyewaOrderState.Success }

    fun profile(token : String){
        setLoading()
        userRepository.profile(token){listUser, error->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) } }
            listUser?.let { user.postValue(it) }
        }
    }

    fun orderStore(token : String, id_penyewa : String, id_produk : String, harga : String,
                   tanggal_mulai_sewa : String, selesai_sewa : String, sisi : String){
        setLoading()
        orderRepository.orderStore(token, id_penyewa, id_produk, harga, tanggal_mulai_sewa, selesai_sewa, sisi){resultBool, error->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) } }
            if (resultBool){ success() }
        }
    }

    fun listenToState() = state
    fun listenToUser() = user

}
sealed class PenyewaOrderState{
    data class IsLoading(var state : Boolean = false) : PenyewaOrderState()
    data class ShowToast(var message : String) : PenyewaOrderState()
    object Success : PenyewaOrderState()
    data class Validate(
        var tanggal_mulai_sewa: String? = null,
        var selesai_sewa: String? = null
    ) : PenyewaOrderState()

    data class HandleLamaSewa(var lamaSewa : String) : PenyewaOrderState()
}