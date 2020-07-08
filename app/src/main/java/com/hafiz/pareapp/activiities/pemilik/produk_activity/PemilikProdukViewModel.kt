package com.hafiz.pareapp.activiities.pemilik.produk_activity

import androidx.lifecycle.ViewModel
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.repositories.ProdukRepository
import com.hafiz.pareapp.utils.SingleLiveEvent

class PemilikProdukViewModel (private val produkRepository: ProdukRepository) : ViewModel(){
    private val state : SingleLiveEvent<PemilikProdukState> = SingleLiveEvent()

    private fun toast(message: String) { state.value = PemilikProdukState.ShowToast(message) }
    private fun setLoading() { state.value = PemilikProdukState.IsLoading(true) }
    private fun hideLoading() { state.value = PemilikProdukState.IsLoading(false) }
    private fun success() { state.value = PemilikProdukState.Success }

    fun validate(panjang: String, lebar: String, masa_berlaku: String, keterangan: String, harga_sewa: String, alamat: String, foto: String?, sisi : String) : Boolean{
        state.value = PemilikProdukState.Reset
        if (masa_berlaku.isEmpty()){
            state.value = PemilikProdukState.Validate(masa_berdiri = "masa berdiri tidak boleh kosong")
            return false
        }
        if (keterangan.isEmpty()){
            state.value = PemilikProdukState.Validate(keterangan = "keterangan tidak boleh kosong")
            return false
        }
        if (harga_sewa.isEmpty()){
            state.value = PemilikProdukState.Validate(harga_sewa = "harga sewa tidak boleh kosong")
            return false
        }
        if (panjang.isEmpty()){
            state.value = PemilikProdukState.Validate(panjang = "panjang tidak boleh kosong")
            return false
        }
        if (lebar.isEmpty()){
            state.value = PemilikProdukState.Validate(lebar = "lebar tidak boleh kosong")
            return false
        }
        if (alamat.isEmpty()){
            state.value = PemilikProdukState.Validate(alamat = "alamat tidak boleh kosong")
            return false
        }
        if (foto != null){
            if (foto.isEmpty()){
                toast("foto tidak boleh kosong")
                return false
            }
        }
        if (sisi.isEmpty()){
            state.value = PemilikProdukState.Validate(sisi = "sisi tidak boleh kosong")
            return false
        }
        return true
    }

    fun tambahproduk(token: String, produk: Produk, urlFoto: String){
        setLoading()
        produkRepository.tambahProduk(token, produk, urlFoto){resultBool, error->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) }}
            if (resultBool){
                success()
            }
        }
    }

    fun updateproduk(token: String, id : String, produk: Produk, urlFoto: String){
        setLoading()
        produkRepository.updateProduk(token, id, produk, urlFoto){resultBool, error->
            error?.let { it.message?.let { message->toast(message) }}
            if (resultBool){
                success()
            }
        }
    }

    fun listenToState() = state
}
sealed class PemilikProdukState{
    data class IsLoading(var state : Boolean = false) : PemilikProdukState()
    data class ShowToast(var message : String) : PemilikProdukState()
    object Success : PemilikProdukState()
    object Reset : PemilikProdukState()
    data class Validate(
        var masa_berdiri : String? = null,
        var keterangan : String? = null,
        var harga_sewa : String? = null,
        var panjang : String? = null,
        var lebar : String? = null,
        var alamat : String? = null,
        var foto : String? = null,
        var sisi : String? = null
    ) : PemilikProdukState()
}