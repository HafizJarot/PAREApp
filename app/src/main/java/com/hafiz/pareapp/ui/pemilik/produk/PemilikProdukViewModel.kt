package com.hafiz.pareapp.ui.pemilik.produk

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hafiz.pareapp.models.Kecamatan
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.repositories.KecamatanRepository
import com.hafiz.pareapp.repositories.ProdukRepository
import com.hafiz.pareapp.utils.ArrayResponse
import com.hafiz.pareapp.utils.SingleLiveEvent
import com.hafiz.pareapp.utils.SingleResponse
import java.lang.Error

class PemilikProdukViewModel (private val produkRepository: ProdukRepository,
                              private val kecamatanRepository: KecamatanRepository) : ViewModel(){
    private val state : SingleLiveEvent<PemilikProdukState> = SingleLiveEvent()
    private val product = MutableLiveData<Produk>(Produk())
    private val kecamatan = MutableLiveData<List<Kecamatan>>()

    private fun toast(message: String) { state.value = PemilikProdukState.ShowToast(message) }
    private fun setLoading() { state.value = PemilikProdukState.IsLoading(true) }
    private fun hideLoading() { state.value = PemilikProdukState.IsLoading(false) }
    private fun success() { state.value = PemilikProdukState.Success }
    private fun successUpdate() { state.value = PemilikProdukState.SuccessUpdate }
    private fun successDelete() { state.value = PemilikProdukState.SuccessDelete }

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
            toast("sisi tidak boleh kosong")
            return false
        }
        return true
    }

    fun createProduct(token: String, produk: Produk, urlFoto: String){
        setLoading()
        produkRepository.createProduct(token, produk, urlFoto , object : SingleResponse<Produk>{
            override fun onSuccess(data: Produk?) {
                hideLoading()
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun setCurrentProduct(p: Produk) = product.postValue(p)
    fun setCurrentProductImage(imgPath : String){
        val temp = product.value!!
        temp.foto = imgPath
        println(imgPath)
        product.postValue(temp)
    }

    fun updateproduk(token: String, id : String, produk: Produk) {
        setLoading()
        val foto = produk.foto
        produk.foto = null
        println("foto ${produk.foto}}")
        produkRepository.updateProduct(token, id, produk, object : SingleResponse<Produk>{
            override fun onSuccess(data: Produk?) {
                hideLoading()
                data?.let {
                    if(foto != null){
                        println("ydhmwb"+foto)
                        updatePhotoProduk(token, id, foto)
                    }else{
                        successUpdate()
                    }
                }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    private fun updatePhotoProduk(token: String, id : String, urlFoto: String){
        setLoading()
        produkRepository.updateProductPhoto(token, id, urlFoto, object : SingleResponse<Produk>{
            override fun onSuccess(data: Produk?) {
                hideLoading()
                data?.let {
                    successUpdate()
                }

            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun fetchKecamatan(){
        setLoading()
        kecamatanRepository.fetchKecamaatan(object : ArrayResponse<Kecamatan>{
            override fun onSuccess(datas: List<Kecamatan>?) {
                hideLoading()
                datas?.let { kecamatan.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun listenToCurrentProduct() = product
    fun listenToKecamatan() = kecamatan
    fun listenToState() = state
}
sealed class PemilikProdukState{
    data class IsLoading(var state : Boolean = false) : PemilikProdukState()
    data class ShowToast(var message : String) : PemilikProdukState()
    object Success : PemilikProdukState()
    object SuccessUpdate : PemilikProdukState()
    object SuccessDelete : PemilikProdukState()
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