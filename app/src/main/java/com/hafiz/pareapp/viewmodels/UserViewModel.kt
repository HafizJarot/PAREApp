package com.hafiz.pareapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.webservices.ApiClient
import com.hafiz.pareapp.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel(){
    private val api = ApiClient.instance()
    private var state : SingleLiveEvent<UserState> = SingleLiveEvent()
    private var currentUser = MutableLiveData<User>()

    fun setLoading() { state.value = UserState.IsLoading(true)}
    fun hideLoading() { state.value = UserState.IsLoading(false)}

    fun validate(email: String, password : String, name : String?) : Boolean{
        state.value = UserState.Reset
        if(!PareUtils.isValidEmail(email)){
            state.value = UserState.Validate(email = "Email tidak valid")
            return false
        }

        if(!PareUtils.isValidPassword(password)){
            state.value = UserState.Validate(password = "Password setidaknya delapan karakter")
            return false
        }

        if(name != null && name.isEmpty()){
            state.value = UserState.Validate(name = "Nama tidak boleh kosong")
            return false
        }
        return true
    }

    fun loginPenyewa(email: String, passsword : String){
        setLoading()
        api.loginPenyewa(email, passsword).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                hideLoading()
                state.value = UserState.ShowToast(t.message.toString())
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if(response.isSuccessful){
                    val b = response.body()
                    b?.let {
                        if(it.status){
                            state.value = UserState.Success(it.data.token!!)
                        }else{
                            state.value = UserState.ShowToast("Login gagal")
                        }
                    }
                }else{
                    state.value = UserState.ShowAlert("Tidak dapat masuk. Periksa kembali email dan password anda")
                }
                hideLoading()
            }
        })
    }

    fun listenUIState() = state

}

sealed class UserState {
    object Reset : UserState()
    data class IsLoading(var state : Boolean) : UserState()
    data class ShowToast(var message : String) : UserState()
    data class ShowAlert(var message : String) : UserState()
    data class Success(var param : String) : UserState()
    data class Validate(var email : String? = null, var password : String? = null, var name : String? = null) : UserState()
}