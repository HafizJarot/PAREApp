package com.hafiz.pareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.viewmodels.UserState
import com.hafiz.pareapp.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        doLogin()
        userViewModel.listenUIState().observer(this, Observer { handleUIState(it) })
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val e = et_email.text.toString().trim()
            val p = et_password.text.toString().trim()
            if(userViewModel.validate(e, p, null)){
                userViewModel.loginPenyewa(e, p)
            }
        }
    }

    private fun handleUIState(it: UserState){
        when(it){
            is UserState.ShowToast -> toast(it.message)
            is UserState.ShowAlert -> showAlert(it.message)
            is UserState.IsLoading -> {
                btn_login_penyedia.isEnabled = !it.state
                btn_login.isEnabled = !it.state
                loading.isIndeterminate = it.state
            }
            is UserState.Reset -> {
                setErrorEmail(null)
                setErrorPassword(null)
            }
            is UserState.Validate -> {
                it.email?.let { x -> setErrorEmail(x) }
                it.password?.let { x -> setErrorPassword(x) }
            }
            is UserState.Success -> {
                PareUtils.setToken(this@LoginActivity, it.param)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun showAlert(m : String){
        AlertDialog.Builder(this).apply {
            setMessage(m)
            setPositiveButton("Mengerti"){ d, w ->
                d.dismiss()
            }
        }.show()
    }

    private fun toast(m : String) = Toast.makeText(this, m , Toast.LENGTH_LONG).show()
    private fun setErrorEmail(err : String?) { in_email.error = err }
    private fun setErrorPassword(err : String?) { in_password.error = err }

    override fun onResume() {
        super.onResume()
        if(PareUtils.getToken(this@LoginActivity) != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}
