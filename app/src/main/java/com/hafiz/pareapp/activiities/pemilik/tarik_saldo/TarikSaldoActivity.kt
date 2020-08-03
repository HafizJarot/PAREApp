package com.hafiz.pareapp.activiities.pemilik.tarik_saldo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.extensions.gone
import com.hafiz.pareapp.extensions.visible
import com.hafiz.pareapp.fragments.pemilik.profile.PemilikProfileViewModel
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.activity_tarik_saldo.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TarikSaldoActivity : AppCompatActivity() {

    private val tarikSaldoViewModel : TarikSaldoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarik_saldo)

        tarikSaldoViewModel.listenToState().observer(this, Observer { handleUiState(it) })
        ambilUang()
    }

    private fun handleUiState(it: TarikSaldoState) {
        when(it){
            is TarikSaldoState.Loading -> handleLoading(it.state)
            is TarikSaldoState.ShowToast -> alertGagal(it.message)
            is TarikSaldoState.SuccessAmbilUang -> alertSuccess("silahkan di tunggu")
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state){
            loading.visible()
        }else{
            loading.gone()
        }
        btn_ambil_saldo.isEnabled = !state
    }


    private fun alertGagal(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun alertSuccess(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
                finish()
            }
        }.show()
    }

    private fun ambilUang(){
        btn_ambil_saldo.setOnClickListener {
            val token = "Bearer ${PareUtils.getToken(this@TarikSaldoActivity)}"
            val saldo = ed_ambil_saldo.text.toString().trim()
            val namaBank = ed_nama_bank.text.toString().trim()
            val namaRekening = ed_nama_rekening.text.toString().trim()
            val nomorRekening = ed_nomor_rekening.text.toString().trim()
            if (tarikSaldoViewModel.validate(saldo, namaBank, namaRekening, nomorRekening)){
                tarikSaldoViewModel.ambilUang(token, saldo, namaBank, namaRekening, nomorRekening)
            }
        }
    }
}
