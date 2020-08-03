package com.hafiz.pareapp.activiities.penyewa.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.hafiz.pareapp.activiities.penyewa.order.PenyewaOrderActivity
import com.hafiz.pareapp.models.Produk
import kotlinx.android.synthetic.main.activity_penyewa_detail_produk.*
import kotlinx.android.synthetic.main.activity_penyewa_detail_produk.iv_produk
import kotlinx.android.synthetic.main.content_penyewa_detail_produk.*
import android.content.Context
import android.hardware.Sensor
import androidx.appcompat.app.AlertDialog
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.penyewa.panoramic_view.PanoramicView


class PenyewaDetailProdukActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penyewa_detail_produk)
        setSupportActionBar(toolbar)
        btn_order.setOnClickListener {
            startActivity(Intent(this@PenyewaDetailProdukActivity,PenyewaOrderActivity::class.java).apply {
                putExtra("PRODUK", getPassedProduk())
                putExtra("TANGGAL_MULAI", getPassedTanggalMulai())
                putExtra("LAMA_SEWA", getPassedLamaSewa())
            })
        }

        getPassedProduk()?.let {
            supportActionBar?.title = it.user!!.nama_perusahaan
            iv_produk.load(it.foto)
            tv_harga.text = it.harga_sewa.toString()
            tv_ukuran.text = "lebar ${it.lebar} x panjang ${it.panjang}"
            tv_alamat.text = it.alamat
            tv_keterangan.text = it.keterangan
        }

        setupSensorManager()
        goToPanoramicView()
    }

    private fun goToPanoramicView(){
        iv_produk.setOnClickListener {
            startActivity(Intent(this, PanoramicView::class.java).apply {
                putExtra("url", getPassedProduk()!!.foto.toString())
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


    private fun getPassedTanggalMulai() = intent.getStringExtra("TANGGAL_MULAI")
    private fun getPassedLamaSewa() = intent.getStringExtra("LAMA_SEWA")
    private fun getPassedProduk() : Produk? = intent.getParcelableExtra("PRODUK")
}
