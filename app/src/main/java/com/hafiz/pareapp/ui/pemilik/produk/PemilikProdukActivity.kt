package com.hafiz.pareapp.ui.pemilik.produk


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import coil.api.load
import com.fxn.pix.Pix
import com.github.dhaval2404.imagepicker.ImagePicker
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Kecamatan
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.extensions.showToast
import kotlinx.android.synthetic.main.pemilik_activity_produk.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class PemilikProdukActivity : AppCompatActivity() {

    private val pemilikProdukViewModel : PemilikProdukViewModel by viewModel()
    private lateinit var sisi : String
    private lateinit var type : String
    private val IMAGE_REQ_CODE = 101
    private var imageUrl = ""
    private var kcmtn : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pemilik_activity_produk)
        isUpdate()
        if (isInsert()) fetchKecamatan()
        observe()
        chooseImage()
        et_masaberdiri.isFocusableInTouchMode = false
        setSpinner()
        setSpinnerType()
        setDate()
        fill()
    }

    private fun observe(){
        observeState()
        observeCurrentProduct()
        if (isInsert()) observeKecamatan()
    }

    private fun observeState() = pemilikProdukViewModel.listenToState().observer(this, Observer { handleUI(it) })
    private fun observeCurrentProduct() = pemilikProdukViewModel.listenToCurrentProduct().observe(this, Observer { handleCurrentProduct(it) })
    private fun observeKecamatan() = pemilikProdukViewModel.listenToKecamatan().observe(this, Observer { handleKecamatan(it) })

    private fun handleKecamatan(list: List<Kecamatan>?) {
        list?.let { kcm ->
            val kecamatan = kcm.distinctBy { k -> k.kecamatan }
            val kec : MutableList<String> = mutableListOf()
            kecamatan.map { kec.add(it.kecamatan!!) }
            val adapter = ArrayAdapter(this@PemilikProdukActivity, android.R.layout.simple_spinner_item, kec)
                .apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            spinner_kecamatan.adapter = adapter
            spinner_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    kcmtn = kcm.find { k -> k.kecamatan == kec[position] }!!.id
                }
            }
        }
    }

    private fun setSpinner(){
        val itemSisi = arrayOf("1", "2")
        val adapter = ArrayAdapter(this@PemilikProdukActivity, android.R.layout.simple_spinner_item, itemSisi).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner_sisi.adapter = adapter
        spinner_sisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { sisi = itemSisi[position] }
        }
    }

    private fun setSpinnerType(){
        val item = arrayOf("Perusahaan", "Individu")
        val adapter = ArrayAdapter(this@PemilikProdukActivity, android.R.layout.simple_spinner_item, item).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner_type.adapter = adapter
        spinner_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { type = item[position] }
        }
    }

    private fun isLoading(b: Boolean){
        btn_store.isEnabled = !b
    }

    private fun resetError(){
        setAlamatErr(null)
        setKeteranganErr(null)
        setLebarErr(null)
        setMasaBerdiriErr(null)
        setPanjangErr(null)
        setHargaSewaErr(null)
    }

    private fun validate(it: PemilikProdukState.Validate){
        it.alamat?.let { setAlamatErr(it) }
        it.harga_sewa?.let { setHargaSewaErr(it) }
        it.keterangan?.let { setKeteranganErr(it) }
        it.lebar?.let { setLebarErr(it) }
        it.panjang?.let { setPanjangErr(it) }
        it.masa_berdiri?.let { setMasaBerdiriErr(it) }
    }


    private fun handleUI(it : PemilikProdukState){
        when(it){
            is PemilikProdukState.ShowToast -> showToast(it.message)
            is PemilikProdukState.IsLoading -> isLoading(it.state)
            is PemilikProdukState.Success -> finish()
            is PemilikProdukState.SuccessUpdate -> finish()
            is PemilikProdukState.Reset -> resetError()
            is PemilikProdukState.Validate -> validate(it)
        }
    }

    private fun handleCurrentProduct(product: Produk?){
        product?.let {
            getPassedProduk()?.let { p ->
                et_panjang.setText(it.panjang.toString())
                et_lebar.setText(it.lebar.toString())
                et_masaberdiri.setText((it.masa_berdiri))
                et_keterangan.setText(it.keterangan.toString())
                et_hargasewa.setText(it.harga_sewa.toString())
                et_alamat.setText(it.alamat)
                if(it.foto != null && !it.foto.toString().startsWith("/storage")){
                    iv_product.load(it.foto)
                }else if(it.foto != null){
                    iv_product.load(File(it.foto.toString()))
                }else{}
            } ?: kotlin.run {
                iv_product.load(File(it.foto.toString()))
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

    private fun onPhotoReturned(imgPath : String) = pemilikProdukViewModel.setCurrentProductImage(imgPath)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK && data != null){
//            val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
//            selectedImageUri?.let { onPhotoReturned(it[0]) }
//        }
        if (resultCode == Activity.RESULT_OK) {
            val selectedImageUri = ImagePicker.getFilePath(data)!!
            onPhotoReturned(selectedImageUri)
            iv_product.load(File(selectedImageUri))
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showToast(ImagePicker.getError(data))
        } else {
            showToast("task cancelled")
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
                val foto = pemilikProdukViewModel.listenToCurrentProduct().value!!.foto.toString()
                if (pemilikProdukViewModel.validate(panjang, lebar, masa_berdiri, keterangan, harga_sewa, alamat, foto, sisi)){
                    val tempProduct = pemilikProdukViewModel.listenToCurrentProduct().value!!
                    tempProduct.id_kecamatan = kcmtn
                    tempProduct.sisi = sisi.toInt()
                    tempProduct.panjang = panjang.toInt()
                    tempProduct.lebar = lebar.toInt()
                    tempProduct.masa_berdiri = masa_berdiri
                    tempProduct.keterangan = keterangan
                    tempProduct.harga_sewa = harga_sewa.toInt()
                    tempProduct.alamat = alamat
                    tempProduct.type = type
                    pemilikProdukViewModel.createProduct(token, tempProduct, foto)
                }else{
                    showToast("not valid")
                }
            }
        }else{
            label_produk.text = "Update Papan Reklame"
            btn_store.text = "update"
            btn_store.setOnClickListener {
                try{
                    val token = "Bearer ${PareUtils.getToken(this@PemilikProdukActivity)}"
                    val panjang = et_panjang.text.toString().trim()
                    val lebar = et_lebar.text.toString().trim()
                    val masa_berdiri = et_masaberdiri.text.toString().trim()
                    val keterangan = et_keterangan.text.toString().trim()
                    val harga_sewa = et_hargasewa.text.toString().trim()
                    val alamat = et_alamat.text.toString().trim()
                    val _sisi = sisi
                    var foto : String? = null
                    getPassedProduk()?.let { p ->
                        val fotoFromViewModel = pemilikProdukViewModel.listenToCurrentProduct().value!!.foto
                        foto = if(fotoFromViewModel.equals(p.foto)){ null }else{ fotoFromViewModel }
                    }

                    if (pemilikProdukViewModel.validate(masa_berdiri, keterangan, harga_sewa,panjang, lebar, alamat, foto, _sisi)){
                        val tempProduct = pemilikProdukViewModel.listenToCurrentProduct().value!!
                        tempProduct.sisi = sisi.toInt()
                        tempProduct.panjang = panjang.toInt()
                        tempProduct.lebar = lebar.toInt()
                        tempProduct.masa_berdiri = masa_berdiri
                        tempProduct.keterangan = keterangan
                        tempProduct.harga_sewa = harga_sewa.toInt()
                        tempProduct.alamat = alamat
                        tempProduct.foto = foto
                        pemilikProdukViewModel.updateproduk(token, getPassedProduk()?.id.toString(), tempProduct)
                    }else{
                        showToast("not valid")
                    }
                }catch (e: Exception){
                    showToast(e.message.toString())
                }
            }
        }
    }

    private fun isUpdate() {
        getPassedProduk()?.let {
            iv_product.load(it.foto)
            val p = Produk(it.id,null,null, it.panjang, it.lebar, it.sisi,
                it.foto, it.masa_berdiri, it.keterangan, it.harga_sewa, it.alamat, it.status)
            pemilikProdukViewModel.setCurrentProduct(p)
        }
    }

    private fun isInsert() = intent.getBooleanExtra("IS_INSERT", true)
    private fun getPassedProduk() : Produk? = intent.getParcelableExtra("PRODUK")
    private fun chooseImage(){
        //btn_add_image.setOnClickListener { Pix.start(this, IMAGE_REQ_CODE) }
        btn_add_image.setOnClickListener {
            ImagePicker.with(this).compress(1024).start()
        }
    }
    private fun setAlamatErr(err : String?) { til_alamat.error = err }
    private fun setHargaSewaErr(err : String?) { til_hargasewa.error = err }
    private fun setKeteranganErr(err : String?) { til_keterangan.error = err }
    private fun setLebarErr(err : String?) { til_lebar.error = err }
    private fun setPanjangErr(err : String?) { til_panjang.error = err }
    private fun setMasaBerdiriErr(err : String?) { til_masaberdiri.error = err }
    private fun fetchKecamatan() = pemilikProdukViewModel.fetchKecamatan()
}