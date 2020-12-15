package com.hafiz.pareapp.ui.pemilik.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.ui.login.LoginActivity
import com.hafiz.pareapp.utils.extensions.gone
import com.hafiz.pareapp.utils.extensions.visible
import com.hafiz.pareapp.models.RegisterPemilik
import kotlinx.android.synthetic.main.pemilik_activity_register.*
import kotlinx.android.synthetic.main.pemilik_activity_register.loading
import org.koin.androidx.viewmodel.ext.android.viewModel

class PemilikRegisterActivity : AppCompatActivity(){
    private val pemilikRegisterViewModel : PemilikRegisterViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pemilik_activity_register)

        observe()
        register()
        checkNoIzin()
    }

    private fun observe(){
        observeState()
        observePemilik()
    }

    private fun observeState() = pemilikRegisterViewModel.listenToState().observer(this, Observer { handleUI(it) })
    private fun observePemilik() = pemilikRegisterViewModel.listenToPemilik().observe(this, Observer { handlePemilik(it) })

    private fun setNoIzin(no : String) = pemilikRegisterViewModel.setNoIzin(no)

    @SuppressLint("SetTextI18n")
    private fun handlePemilik(pemilik: Pemilik?) {
        pemilik?.let {
            setNoIzin(it.no_izin!!)
            linear_detail.visible()
            linear_akun.visible()
            txt_no_izin.text = "No Izin : ${it.no_izin}"
            txt_nama_perusahaan.text = "Nama Perusahaan : ${it.nama_perusahaan}"
            txt_alamat.text = "Alamat : ${it.alamat}"
        }
    }

    private fun checkNoIzin(){
        btn_check.setOnClickListener {
            val noIzin = et_no_izin.text.toString()
            if (pemilikRegisterViewModel.validateNoIzin(noIzin)){
                pemilikRegisterViewModel.checkNoIzin(noIzin)
            }
        }
    }

    private fun isLoading (b:Boolean){
        btn_register.isEnabled = !b
        if (b){
            loading.visible()
        }else{
            loading.gone()
        }
    }

    private fun resetError(){
        setNoIzinError(null)
        setEmailError(null)
        setPasswordError(null)
        setConfirmPasswordError(null)
        setNoHpError(null)
    }

    private fun validateError (it:PemilikRegisterState.Validate){
        setNoIzinError(it.noIzin)
        setEmailError(it.email)
        setPasswordError(it.password)
        setConfirmPasswordError(it.confirmPassword)
        setNoHpError(it.noHp)
    }


    private fun handleUI(it: PemilikRegisterState) {
        when(it){
            is PemilikRegisterState.ShowToast -> toast(it.mesagge)
            is PemilikRegisterState.IsLoading -> isLoading(it.state)
            is PemilikRegisterState.Success -> success()
            is PemilikRegisterState.Reset -> resetError()
            is PemilikRegisterState.Validate -> validateError(it)
        }
    }

    private fun register(){
        btn_register.setOnClickListener {
            val noIzin = pemilikRegisterViewModel.getNoIzin().value!!
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            val confirmPassword = et_confirm_password.text.toString().trim()
            val noHp = et_no_hp.text.toString().trim()
            //val alamat = et_alamat.text.toString().trim()

            if (pemilikRegisterViewModel.validate(noIzin, email, password, confirmPassword, noHp)){
                val user = RegisterPemilik(no_izin = noIzin, email = email, password = password,
                    no_hp = noHp)
                pemilikRegisterViewModel.register(user)
            }
        }
    }

    private fun success() {
        AlertDialog.Builder(this@PemilikRegisterActivity).apply {
            setMessage("silahkan menunggu konfirmasi admin, nanti kami kabarin lagi")
            setPositiveButton("Mengerti"){ d, _ ->
                d.dismiss()
                startActivity(Intent(this@PemilikRegisterActivity, LoginActivity::class.java))
                this@PemilikRegisterActivity.finish()
            }
        }.show()
    }

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    private fun setNoIzinError(err : String?){ til_no_izin.error = err }
    private fun setEmailError(err : String?){ til_email.error = err }
    private fun setPasswordError(err : String?){ til_password.error = err }
    private fun setConfirmPasswordError(err : String?){ til_confirm_password.error = err }
    private fun setNoHpError(err : String?){ til_no_hp.error = err }
}