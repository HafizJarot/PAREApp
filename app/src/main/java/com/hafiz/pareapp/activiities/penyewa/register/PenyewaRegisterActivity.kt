package com.hafiz.pareapp.activiities.penyewa.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.login.LoginActivity
import com.hafiz.pareapp.extensions.disable
import com.hafiz.pareapp.extensions.enabled
import com.hafiz.pareapp.extensions.showToast
import kotlinx.android.synthetic.main.penyewa_activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PenyewaRegisterActivity : AppCompatActivity(){

    private val penyewaRegisterViewModel : PenyewaRegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.penyewa_activity_register)
        penyewaRegisterViewModel.listenToState().observer(this, Observer { handleUI(it) })
        register()
    }

    private fun handleUI(it: PenyewaRegisterState) {
        when(it){
            is PenyewaRegisterState.IsLoading -> {
                if (it.state){
                    btn_buat_akun.disable()
                }else{
                    btn_buat_akun.enabled()
                }
            }
            is PenyewaRegisterState.ShowToast -> showToast(it.message)
            is PenyewaRegisterState.Success -> success(it.email)
            is PenyewaRegisterState.Reset -> {
                setNameError(null)
                setEmailError(null)
                setPasswordError(null)
                setConfirmPasswordError(null)
                setAlamatError(null)
            }
            is PenyewaRegisterState.Validate -> {
                it.name?.let { setNameError(it) }
                it.email?.let { setEmailError(it) }
                it.password?.let { setPasswordError(it) }
                it.confirmPassword?.let { setConfirmPasswordError(it) }
                it.alamat?.let { setAlamatError(it) }
            }

        }
    }

    private fun register(){
        btn_buat_akun.setOnClickListener {
            val name = et_nama_lengkap.text.toString().trim()
            val email = et_email.text.toString().trim()
            val pass = et_password.text.toString().trim()
            val cPass = et_confirm_password.text.toString().trim()
            val alamat = et_alamat.text.toString().trim()

            if (penyewaRegisterViewModel.validate(name, email, pass, cPass, alamat)){
                penyewaRegisterViewModel.register(name, email, pass, alamat)
            }
        }
    }

    private fun success(email : String) {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.ThemeOverlay_AppCompat_Dialog_Alert)).apply {
            setMessage("Kami telah mengirim email ke $email. Pastikan anda telah memverifikasi email sebelum login")
            setPositiveButton("Mengerti"){ d, _ ->
                d.cancel()
                startActivity(Intent(this@PenyewaRegisterActivity, LoginActivity::class.java))
                this@PenyewaRegisterActivity.finish()
            }.create().show()
        }
    }

    private fun setNameError(err : String?){ til_nama_lengkap.error = err }
    private fun setEmailError(err : String?){ til_email.error = err }
    private fun setPasswordError(err : String?){ til_password.error = err }
    private fun setConfirmPasswordError(err : String?){ til_confirm_password.error = err }
    private fun setAlamatError(err : String?){ til_alamat.error = err }
}