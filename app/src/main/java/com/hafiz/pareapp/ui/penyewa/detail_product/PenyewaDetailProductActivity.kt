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
import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.ui.penyewa.panoramic_view.PanoramicView
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.penyewa_activity_detail_product.*
import kotlinx.android.synthetic.main.penyewa_content_detail_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PenyewaDetailProductActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private val penyewaDetailProdukViewModel : PenyewaDetailProdukViewModel by viewModel()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.penyewa_activity_detail_product)
        setSupportActionBar(toolbar)
        setUI()
        setDateStart()
        setDateEnd()
        lanjutkan()
        setupSensorManager()
        goToPanoramicView()
        //calcDate()
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

    private fun lanjutkan(){
//        btn_order.setOnClickListener {
//            startActivity(
//                Intent(this@PenyewaDetailProductActivity,
//                    PenyewaOrderActivity::class.java).apply {
//                    putExtra("PRODUCT", getPassedProduct())
//                })
//        }
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

    private fun getDiffMonth() = penyewaDetailProdukViewModel.listenToDiffMonth().value
    private fun getStartDate() = penyewaDetailProdukViewModel.listenToStartDate().value
    private fun getEndDate() = penyewaDetailProdukViewModel.listenToEndDate().value

    private fun setDateStart(){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
            txt_tanggal_mulai.text = simpleDateFormat.format(cal.time)
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

    private fun calcDate() {
        println(getStartDate())
        println(getEndDate())
    }

    private fun getPassedProduct() : Produk? = intent.getParcelableExtra("PRODUCT")
    private fun getPassedCompany() : Pemilik? = intent.getParcelableExtra("COMPANY")
}
