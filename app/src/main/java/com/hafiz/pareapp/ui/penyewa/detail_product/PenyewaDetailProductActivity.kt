package com.hafiz.pareapp.ui.penyewa.detail_product

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.CreateOrder
import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.ui.penyewa.panoramic_view.PanoramicView
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.extensions.showToast
import kotlinx.android.synthetic.main.penyewa_activity_detail_product.*
import kotlinx.android.synthetic.main.penyewa_content_detail_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PenyewaDetailProductActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private val penyewaDetailProdukViewModel : PenyewaDetailProdukViewModel by viewModel()
    private var startMonth : Int? = null
    private var endMonth : Int? = null
    private var startDate : String? = null
    private var endDate : String? = null
    private var idPenyewa : String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.penyewa_activity_detail_product)
        setSupportActionBar(toolbar)
        setUI()
        observe()
        setDateStart()
        setDateEnd()
        setupSensorManager()
        goToPanoramicView()
    }

    private fun observe(){
        observeState()
        observeUer()
    }

    private fun observeState() = penyewaDetailProdukViewModel.listenToState().observe(this, androidx.lifecycle.Observer { handleUiState(it) })

    private fun handleUiState(state: DetailProdukState?) {
        state?.let {
            when(it){
                is DetailProdukState.Loading -> handleLoading(it.state)
                is DetailProdukState.ShowToast -> showToast(it.message)
                is DetailProdukState.Success -> handleSuccess()
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        if (b) btn_order.isEnabled = !b
    }

    private fun handleSuccess() {
        AlertDialog.Builder(this).apply {
            setMessage("terima kasih")
            setPositiveButton("ya"){dialog, which ->
                dialog.dismiss()
                finish()
            }
        }.show()
    }


    @SuppressLint("SetTextI18n")
    private fun setUI(){
        getPassedProduct()?.let {
            supportActionBar?.title = getPassedCompany()?.nama_perusahaan
            iv_produk.load(it.foto)
            tv_harga.text = "Harga : ${PareUtils.setToIDR(it.harga_sewa!!)} / Bulan"
            tv_ukuran.text = "lebar ${it.lebar} x panjang ${it.panjang}"
            tv_alamat.text = "Alamat : ${it.alamat}"
            tv_keterangan.text = "Keterangan : ${it.keterangan}"
        }
    }

    private fun goToPanoramicView() {
        iv_produk.setOnClickListener {
            startActivity(Intent(this, PanoramicView::class.java).apply {
                putExtra("url", getPassedProduct()!!.foto.toString())
            })
        }
    }

    //cek apakah hp memiliki sensor gyro atau tidak
    private fun setupSensorManager(){
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorList = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE)
        if(sensorList.isEmpty()){
            showAlertNoSensor()
        }
    }

    private fun showAlertNoSensor(){
        AlertDialog.Builder(this).apply {
            setMessage(resources.getString(R.string.no_sensor))
            setPositiveButton(resources.getString(R.string.ok)){ d, _ ->
                d.dismiss()
            }
        }.show()
    }

    private fun setDateStart(){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
            txt_tanggal_mulai.text = simpleDateFormat.format(cal.time)
            startMonth = month
            startDate = simpleDateFormat.format(cal.time)
            //penyewaDetailProdukViewModel.setStartDate(simpleDateFormat.format(cal.time))
        }

        btn_open_tgl_mulai.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).apply {
                datePicker.minDate = cal.timeInMillis
            }.show()
        }
    }

    private fun setDateEnd(){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
            txt_tanggal_selesai.text = simpleDateFormat.format(cal.time)
            endMonth = month
            endDate = simpleDateFormat.format(cal.time)
            calcDate()
            //penyewaDetailProdukViewModel.setEndDate(simpleDateFormat.format(cal.time))
        }

        btn_open_tgl_selesai.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).apply {
                datePicker.minDate = cal.timeInMillis
            }.show()
        }
    }

    private fun order(){
        val token = "Bearer ${PareUtils.getToken(this@PenyewaDetailProductActivity)}"
        val id_pemilik = getPassedCompany()?.id
        val id_produk = getPassedProduct()?.id
        val harga = getPassedProduct()?.harga_sewa
        val tanggal_mulai_sewa = startDate
        val tanggal_selesai_sewa = endDate
        val sisi = getPassedProduct()?.sisi
            val diffMonth = endMonth!!.minus(startMonth!!)
        val createOrder = CreateOrder(idPemilik = id_pemilik.toString(), idProduk = id_produk.toString(), harga = harga.toString(),
            tanggalMulaiSewa = tanggal_mulai_sewa!!, selesaiSewa = tanggal_selesai_sewa!!, sisi = sisi.toString())

        val paymentMidtrans = PaymentMidtrans()
        paymentMidtrans.initPayment(this@PenyewaDetailProductActivity, penyewaDetailProdukViewModel, token, createOrder)
        btn_order.setOnClickListener {
            val _harga = getPassedProduct()?.harga_sewa
            //val _selesai_sewa = getPassedLamaSewa()
            val _sisi = getPassedProduct()?.sisi
            paymentMidtrans.showPayment(this@PenyewaDetailProductActivity, idPenyewa!!
                ,_harga!!.times(_sisi!!), if (diffMonth == 0) 1 else diffMonth, getPassedProduct()?.alamat!!)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calcDate() {
        val diffMonth = endMonth!!.minus(startMonth!!)
        val harga = if (diffMonth == 0){
            getPassedProduct()?.harga_sewa
        }else{
            getPassedProduct()?.harga_sewa!!.times(diffMonth)
        }
        txt_harga.text = "Harga : ${PareUtils.setToIDR(harga!!)}"
        txt_sisi.text =  "Sisi : ${getPassedProduct()?.sisi!!}"
        txt_total_harga.text = "Total Harga : ${PareUtils.setToIDR(harga.times(getPassedProduct()?.sisi!!))}"
        btn_order.isEnabled = true
        order()
    }

    private fun getPassedProduct() : Produk? = intent.getParcelableExtra("PRODUCT")
    private fun getPassedCompany() : Pemilik? = intent.getParcelableExtra("COMPANY")

    private fun currentUser() = penyewaDetailProdukViewModel.currentUser("Bearer ${PareUtils.getToken(this@PenyewaDetailProductActivity)}")
    private fun observeUer() = penyewaDetailProdukViewModel.listenToCurrentuser().observe(this, androidx.lifecycle.Observer { handleCurrentUser(it) })

    private fun handleCurrentUser(user: User?) {
        user?.let { idPenyewa = it.id.toString() }
    }

    override fun onResume() {
        super.onResume()
        currentUser()
    }
}
