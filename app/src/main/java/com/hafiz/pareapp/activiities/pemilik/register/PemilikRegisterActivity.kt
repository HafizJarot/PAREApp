package com.hafiz.pareapp.activiities.pemilik.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.login.LoginActivity
import com.hafiz.pareapp.extensions.gone
import com.hafiz.pareapp.extensions.visible
import com.hafiz.pareapp.models.RegisterPemilik
import kotlinx.android.synthetic.main.pemilik_activity_register.*
import kotlinx.android.synthetic.main.pemilik_activity_register.loading
import org.koin.androidx.viewmodel.ext.android.viewModel

class PemilikRegisterActivity : AppCompatActivity(){
    private val pemilikRegisterViewModel : PemilikRegisterViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pemilik_activity_register)

        pemilikRegisterViewModel.listenToState().observer(this, Observer { handleUI(it) })
        register()
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
        setNamaPerusahaanError(null)
        setEmailError(null)
        setPasswordError(null)
        setConfirmPasswordError(null)
        setNoHpError(null)
        setAlamatError(null)
    }

    private fun validateError (it:PemilikRegisterState.Validate){
        setNoIzinError(it.noIzin)
        setNamaPerusahaanError(it.namaPerusahaan)
        setEmailError(it.email)
        setPasswordError(it.password)
        setConfirmPasswordError(it.confirmPassword)
        setNoHpError(it.noHp)
        setAlamatError(it.alamat)
    }


    private fun handleUI(it: PemilikRegisterState) {
        when(it){
            is PemilikRegisterState.ShowToast -> toast(it.mesagge)
            is PemilikRegisterState.IsLoading -> isLoading(it.state)
            is PemilikRegisterState.Success -> success(it.email)
            is PemilikRegisterState.Reset -> resetError()
            is PemilikRegisterState.Validate -> validateError(it)
        }
    }

    private fun register(){
        btn_register.setOnClickListener {
            val noIzin = et_no_izin.text.toString().trim()
            val namaPerusahaan = et_nama_perusahaan.text.toString().trim()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            val confirmPassword = et_confirm_password.text.toString().trim()
            val noHp = et_no_hp.text.toString().trim()
            val alamat = et_alamat.text.toString().trim()

            if (pemilikRegisterViewModel.validate(noIzin, namaPerusahaan, email, password, confirmPassword, noHp, alamat)){
                val user = RegisterPemilik(no_izin = noIzin, nama_perusahaan = namaPerusahaan, email = email, password = password,
                    no_hp = noHp, alamat = alamat)
                pemilikRegisterViewModel.register(user)
            }
        }
    }

    private fun success(email : String) {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.ThemeOverlay_AppCompat_Dialog_Alert)).apply {
            setMessage("Kami telah mengirim email ke $email. Pastikan anda telah memverifikasi email sebelum login")
            setPositiveButton("Mengerti"){ d, _ ->
                d.cancel()
                startActivity(Intent(this@PemilikRegisterActivity, LoginActivity::class.java))
                this@PemilikRegisterActivity.finish()
            }.create().show()
        }
    }

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    private fun setNoIzinError(err : String?){ til_no_izin.error = err }
    private fun setNamaPerusahaanError(err : String?){ til_nama_perusahaan.error = err }
    private fun setEmailError(err : String?){ til_email.error = err }
    private fun setPasswordError(err : String?){ til_password.error = err }
    private fun setConfirmPasswordError(err : String?){ til_confirm_password.error = err }
    private fun setNoHpError(err : String?){ til_no_hp.error = err }
    private fun setAlamatError(err : String?){ til_alamat.error = err }

}