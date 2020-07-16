package com.hafiz.pareapp.activiities.pemilik.produk


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import coil.api.load

import com.fxn.pix.Pix
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.activity_pemilik_produk.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PemilikProdukActivity : AppCompatActivity() {

    private val pemilikProdukViewModel : PemilikProdukViewModel by viewModel()
    private lateinit var sisi : String
    private val IMAGE_REQ_CODE = 101
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemilik_produk)
        chooseImage()
        et_masaberdiri.isFocusableInTouchMode = false
        setSpinner()
        setDate()
        pemilikProdukViewModel.listenToState().observer(this@PemilikProdukActivity, Observer { handleUI(it) })
        fill()
    }

    private fun setSpinner(){
        val itemSisi = arrayOf("1", "2")
        val adapter = ArrayAdapter(this@PemilikProdukActivity, android.R.layout.simple_spinner_item, itemSisi).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner_sisi.adapter = adapter
        spinner_sisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sisi = itemSisi[position]
            }

        }
    }

    private fun handleUI(it : PemilikProdukState){
        when(it){
            is PemilikProdukState.ShowToast -> toast(it.message)
            is PemilikProdukState.IsLoading -> {
                btn_store.isEnabled = !it.state
            }
            is PemilikProdukState.Success -> finish()
            is PemilikProdukState.Reset -> {
                setAlamatErr(null)
                setKeteranganErr(null)
                setLebarErr(null)
                setMasaBerdiriErr(null)
                setPanjangErr(null)
                setHargaSewaErr(null)
                //setSisiErr(null)
            }
            is PemilikProdukState.Validate -> {
                it.alamat?.let { setAlamatErr(it) }
                it.harga_sewa?.let { setHargaSewaErr(it) }
                it.keterangan?.let { setKeteranganErr(it) }
                it.lebar?.let { setLebarErr(it) }
                it.panjang?.let { setPanjangErr(it) }
                it.masa_berdiri?.let { setMasaBerdiriErr(it) }
            }
        }
    }

    private fun setDate(){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
            et_masaberdiri.setText(simpleDateFormat.format(cal.time))
        }
        et_masaberdiri.setOnClickListener {
            DatePickerDialog(this@PemilikProdukActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK && data != null){
            val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            selectedImageUri?.let {
                iv_product.load(File(it[0]))
                imageUrl = it[0]
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fill(){
        if (isInsert()){
            label_produk.text = "Tambah Papan Reklame"
            btn_store.text = "tambah"
            btn_store.setOnClickListener {
                val token = "Bearer ${PareUtils.getToken(this@PemilikProdukActivity)}"
                val panjang = et_panjang.text.toString().trim()
                val lebar = et_lebar.text.toString().trim()
                val masa_berdiri = et_masaberdiri.text.toString().trim()
                val keterangan = et_keterangan.text.toString().trim()
                val harga_sewa = et_hargasewa.text.toString().trim()
                val alamat = et_alamat.text.toString().trim()
                val sisi = sisi

                if (pemilikProdukViewModel.validate(masa_berdiri, keterangan, harga_sewa,panjang, lebar, alamat, imageUrl, sisi)){
                    val kirimKeProduk = Produk(panjang = panjang.toInt(), lebar = lebar.toInt(), masa_berdiri = masa_berdiri,
                        keterangan = keterangan, harga_sewa = harga_sewa.toInt(), alamat = alamat, sisi = sisi.toInt())
                    pemilikProdukViewModel.tambahproduk(token, kirimKeProduk, imageUrl)
                }else{
                    toast("not valid")
                }
            }
        }else{
            isUpdate()
            label_produk.text = "Update Papan Reklame"
            btn_store.text = "update"
            btn_store.setOnClickListener {
                val token = "Bearer ${PareUtils.getToken(this@PemilikProdukActivity)}"
                val panjang = et_panjang.text.toString().trim()
                val lebar = et_lebar.text.toString().trim()
                val masa_berdiri = et_masaberdiri.text.toString().trim()
                val keterangan = et_keterangan.text.toString().trim()
                val harga_sewa = et_hargasewa.text.toString().trim()
                val alamat = et_alamat.text.toString().trim()
                val _sisi = sisi


                if (pemilikProdukViewModel.validate(masa_berdiri, keterangan, harga_sewa,panjang, lebar, alamat, null, _sisi)){
                    val kirimKeProduk = Produk(panjang = panjang.toInt(), lebar = lebar.toInt(), masa_berdiri = masa_berdiri,
                        keterangan = keterangan, harga_sewa = harga_sewa.toInt(), alamat = alamat, sisi = _sisi.toInt())
                    pemilikProdukViewModel.updateproduk(token, getPassedProduk()?.id.toString(), kirimKeProduk, imageUrl)
                }else{
                    toast("not valid")
                }
            }
        }
    }

    private fun isUpdate() {
        getPassedProduk()?.let {
            et_panjang.setText(it.panjang.toString())
            et_lebar.setText(it.lebar.toString())
            et_masaberdiri.setText((it.masa_berdiri))
            et_keterangan.setText(it.keterangan!!)
            et_hargasewa.setText(it.harga_sewa.toString())
            et_alamat.setText(it.alamat)
            //spinner_sisi.setText(it.sisi.toString())
            iv_product.load(it.foto)
        }
    }

    private fun isInsert() = intent.getBooleanExtra("IS_INSERT", true)
    private fun getPassedProduk() : Produk? = intent.getParcelableExtra("PRODUK")
    private fun chooseImage(){ btn_add_image.setOnClickListener { Pix.start(this, IMAGE_REQ_CODE) } }
    private fun setAlamatErr(err : String?) { til_alamat.error = err }
    private fun setHargaSewaErr(err : String?) { til_hargasewa.error = err }
    private fun setKeteranganErr(err : String?) { til_keterangan.error = err }
    private fun setLebarErr(err : String?) { til_lebar.error = err }
    private fun setPanjangErr(err : String?) { til_panjang.error = err }
    private fun setMasaBerdiriErr(err : String?) { til_masaberdiri.error = err }
    private fun toast(message : String) = Toast.makeText(this@PemilikProdukActivity, message, Toast.LENGTH_LONG).show()

}