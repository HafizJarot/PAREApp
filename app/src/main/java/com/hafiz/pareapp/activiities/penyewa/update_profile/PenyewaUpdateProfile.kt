package com.hafiz.pareapp.activiities.penyewa.update_profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.activity_edit_profile_penyewa.*
import kotlinx.android.synthetic.main.penyewa_fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class UpdateProfileActivity : AppCompatActivity() {

    private val updateProfilViewModel : UpdateProfilViewModel by viewModel()
    private var imgUrl = ""
    private val REQ_CODE_PIX = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_penyewa)
        //setSupportActionBar(toolbar)
        setUi()
        //btn_add_image.setOnClickListener { Pix.start(this, REQ_CODE_PIX) }
        observer()
        updateProfil()
    }

    private fun setUi(){
        getPassedUser()?.let {
            //img_user.load(it.avatar)
            et_nama.setText(it.name)
            et_email.setText(it.email)
        }
    }

    private fun updateProfil() {
        btn_simpan_profil.setOnClickListener {
            val token = "Bearer ${PareUtils.getToken(this@UpdateProfileActivity)}"
            val name = et_nama.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (validate(name)){
                updateProfilViewModel.updateProfile(token, name, password, imgUrl)
            }
        }
    }

    private fun validate(name : String) : Boolean{
        if(name.isEmpty()){
            toast("nama tidak boleh kosong")
            return false
        }
        return true
    }

    private fun observer() {
        updateProfilViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    }

    private fun handleUiState(it: PenyewaUpdateProfilState) {
        when(it){
            is PenyewaUpdateProfilState.Loading -> handleLoading(it.state)
            is PenyewaUpdateProfilState.ShowToast -> toast(it.message)
            is PenyewaUpdateProfilState.Success -> handleSuccess()
        }
    }

    private fun handleLoading(state: Boolean) {
        btn_simpan_profil.isEnabled = !state
    }

    private fun handleSuccess() {
        finish()
        toast("berhasil mengupdate profile")
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == REQ_CODE_PIX && resultCode == Activity.RESULT_OK && data != null){
//            val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
//            selectedImageUri?.let {
//                img_user.load(File(it[0]))
//                imgUrl = it[0]
//            }
//        }
//    }

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun getPassedUser() : User? = intent.getParcelableExtra("USER")
}