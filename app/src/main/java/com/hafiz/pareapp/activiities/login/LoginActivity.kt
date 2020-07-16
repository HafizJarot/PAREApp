package com.hafiz.pareapp.activiities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.pemilik.main.PemilikMainActivity
import com.hafiz.pareapp.activiities.pemilik.register.PemilikRegisterActivity
import com.hafiz.pareapp.activiities.penyewa.main.PenyewaMainActivity
import com.hafiz.pareapp.activiities.penyewa.register.PenyewaRegisterActivity
import com.hafiz.pareapp.extensions.showInfoAlert
import com.hafiz.pareapp.extensions.showToast
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel : LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        loginViewModel.listenToState().observer(this, Observer { handleUIState(it) })
        setSpinner()

        btn_register_penyewa.setOnClickListener {
            startActivity(Intent(this@LoginActivity, PenyewaRegisterActivity::class.java))
        }
        btn_register_pemilik.setOnClickListener {
            startActivity(Intent(this@LoginActivity, PemilikRegisterActivity::class.java))
        }
    }

    private fun isLoading(b: Boolean){
        btn_login.isEnabled = !b
        btn_register_pemilik.isEnabled = !b
        btn_register_penyewa.isEnabled = !b
        loading.isIndeterminate = b
    }

    private fun successPenyewa(token: String, role: Boolean){
        PareUtils.setToken(this@LoginActivity, token)
        PareUtils.setRole(this, role)
        startActivity(Intent(this@LoginActivity, PenyewaMainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }).also { finish() }
    }

    private fun successPemilik(token: String, role: Boolean){
        PareUtils.setToken(this@LoginActivity, token)
        PareUtils.setRole(this, role)
        startActivity(Intent(this@LoginActivity, PemilikMainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }).also { finish() }
    }

    private fun handleUIState(it: LoginState){
        when(it){
            is LoginState.ShowToast -> showToast(it.message)
            is LoginState.ShowAlert -> showInfoAlert(it.message)
            is LoginState.IsLoading -> isLoading(it.state)
            is LoginState.Reset -> {
                setErrorEmail(null)
                setErrorPassword(null)
            }
            is LoginState.Validate -> {
                it.email?.let { x -> setErrorEmail(x) }
                it.password?.let { x -> setErrorPassword(x) }
            }
            is LoginState.SuccessPenyewa -> successPenyewa(it.token, it.role)
            is LoginState.SuccessPemilik -> successPemilik(it.token, it.role)
        }
    }

    private fun setSpinner(){
        val list_of_roles = arrayOf("Penyewa", "Pemilik")
        val adapter = ArrayAdapter(this@LoginActivity, android.R.layout.simple_spinner_item, list_of_roles).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner_role.adapter = adapter
        spinner_role.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                btn_login.setOnClickListener {
                    val email = et_email.text.toString().trim()
                    val pass = et_password.text.toString().trim()
                    //val email = "penyewa@gmail.com"
                    //val pass = "penyewa12345"
                    if(loginViewModel.validate(email, pass)){
                        loginViewModel.login(email, pass, position)
                    }
                }
            }

        }
    }


    private fun setErrorEmail(err : String?) { in_email.error = err }
    private fun setErrorPassword(err : String?) { in_password.error = err }

    override fun onResume() {
        super.onResume()
        PareUtils.getToken(this)?.let { token ->
            PareUtils.getRole(this)?.let { role ->
                if (role) startActivity(Intent(this@LoginActivity, PemilikMainActivity::class.java))
                else startActivity(Intent(this@LoginActivity, PenyewaMainActivity::class.java))
                finish()
            }
        }
    }
}
