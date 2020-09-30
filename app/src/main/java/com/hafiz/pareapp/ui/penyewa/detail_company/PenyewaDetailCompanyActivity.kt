package com.hafiz.pareapp.ui.penyewa.detail_company

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.hafiz.pareapp.ui.penyewa.order.PenyewaOrderActivity
import kotlinx.android.synthetic.main.penyewa_activity_detail_company.*
import kotlinx.android.synthetic.main.penyewa_activity_detail_company.iv_produk
import kotlinx.android.synthetic.main.penyewa_content_detail_company.*
import android.content.Context
import android.hardware.Sensor
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.ui.penyewa.detail_product.PenyewaDetailProductActivity
import com.hafiz.pareapp.ui.penyewa.panoramic_view.PanoramicView
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.extensions.gone
import com.hafiz.pareapp.utils.extensions.showToast
import com.hafiz.pareapp.utils.extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel


class PenyewaDetailCompanyActivity : AppCompatActivity(), CompanyClickListener {

    private val penyewaProductViewModel : PenyewaProductViewModel by viewModel()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.penyewa_activity_detail_company)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getPassedCompany()?.nama_perusahaan
        setUpRecyclerView()
        observe()
    }

    private fun observe() {
        observeState()
        observeProducts()
    }

    private fun observeProducts() = penyewaProductViewModel.listenToProducts().observe(this,  Observer { handleProducts(it) })
    private fun observeState() = penyewaProductViewModel.listenToState().observer(this, Observer { handleUiState(it) })

    private fun handleProducts(list: List<Produk>?) {
        list?.let {
            recycler_view.adapter?.let { adapter ->
                if (adapter is PenyewaProductAdapter){
                    adapter.changeList(it)
                }
            }
        }
    }

    private fun handleUiState(penyewaProductState: PenyewaProductState?) {
        penyewaProductState?.let {
            when(it){
                is PenyewaProductState.Loading -> handleLoading(it.state)
                is PenyewaProductState.ShowToast -> showToast(it.message)
            }
        }
    }

    private fun handleLoading(b: Boolean) = if (b) loading.visible() else loading.gone()

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = PenyewaProductAdapter(mutableListOf(), this@PenyewaDetailCompanyActivity)
            layoutManager = GridLayoutManager(this@PenyewaDetailCompanyActivity, 2)
        }
    }


    private fun fetchProductByCompany() = penyewaProductViewModel.fetchProductByCompany("Bearer ${PareUtils.getToken(this)}", getPassedCompany()?.id.toString())
    private fun getPassedCompany() : Pemilik? = intent.getParcelableExtra("COMPANY")

    override fun click(produk: Produk) = startActivity(Intent(this, PenyewaDetailProductActivity::class.java).apply {
        putExtra("PRODUCT", produk)
        putExtra("COMPANY", getPassedCompany())
    })

    override fun onResume() {
        super.onResume()
        fetchProductByCompany()
    }
}
