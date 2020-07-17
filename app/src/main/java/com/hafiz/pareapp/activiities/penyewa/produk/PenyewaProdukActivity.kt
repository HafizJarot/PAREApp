package com.hafiz.pareapp.activiities.penyewa.produk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafiz.pareapp.MyOnClickListener
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.penyewa.detail.PenyewaDetailProdukActivity
import com.hafiz.pareapp.adapters.penyewa.PenyewaProdukAdapter
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.activity_penyewa_produk.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PenyewaProdukActivity : AppCompatActivity(), MyOnClickListener {

    private val penyewaProdukViewModel : PenyewaProdukViewModel by viewModel()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penyewa_produk)

        rv_penyewa_produk.apply {
            adapter = PenyewaProdukAdapter(mutableListOf(), this@PenyewaProdukActivity)
            layoutManager = GridLayoutManager(this@PenyewaProdukActivity, 2)
        }

        penyewaProdukViewModel.listenToState().observer(this, Observer { handleui(it) })
        penyewaProdukViewModel.listenToProduks().observe(this, Observer {
            rv_penyewa_produk.adapter?.let {adapter ->
                if (adapter is PenyewaProdukAdapter) {
                    adapter.changelist(it)
                }
            }
        })
    }

    private fun handleui(it : PenyewaProdukState){
        when(it){
            is PenyewaProdukState.IsLoading -> {
                if (it.state){
                    pb_penyewa_produk.isIndeterminate = true
                    pb_penyewa_produk.visibility = View.VISIBLE
                }else{
                    pb_penyewa_produk.isIndeterminate = false
                    pb_penyewa_produk.visibility = View.GONE
                }
            }
            is PenyewaProdukState.ShowToast -> toast(it.message)
        }
    }

    override fun click(produk: Produk) {
        startActivity(Intent(this@PenyewaProdukActivity, PenyewaDetailProdukActivity::class.java).apply {
            putExtra("PRODUK", produk)
            putExtra("TANGGAL_MULAI", getPassedTanggalMulai())
            putExtra("LAMA_SEWA", getPassedLamaSewa())
        })
    }

    private fun getPassedTanggalMulai() = intent.getStringExtra("TANGGAL_MULAI")
    private fun getPassedLamaSewa() = intent.getStringExtra("LAMA_SEWA")
    private fun toast(message : String) = Toast.makeText(this@PenyewaProdukActivity, message, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        penyewaProdukViewModel.searchProduk("Bearer ${PareUtils.getToken(this@PenyewaProdukActivity)}", getPassedTanggalMulai(), getPassedLamaSewa())
    }
}