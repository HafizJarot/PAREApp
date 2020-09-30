package com.hafiz.pareapp.ui.pemilik.main.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.ui.login.LoginActivity
import com.hafiz.pareapp.ui.pemilik.tarik_saldo.TarikSaldoActivity
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.pemilik_fragment_profile.*
import kotlinx.android.synthetic.main.penyewa_fragment_profile.btn_logout
import kotlinx.android.synthetic.main.penyewa_fragment_profile.tv_email
import kotlinx.android.synthetic.main.penyewa_fragment_profile.tv_nama
import org.koin.androidx.viewmodel.ext.android.viewModel

class PemilikProfileFragment : Fragment(R.layout.pemilik_fragment_profile){
    private val pemilikProfileViewModel : PemilikProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout()
        pemilikProfileViewModel.listenToCurrentUser().observe(requireActivity(), Observer { handleUser(it) })
        txt_tarik_saldo.setOnClickListener { startActivity(Intent(requireActivity(), TarikSaldoActivity::class.java)) }
//        pemilikProfileViewModel.listenToState().observer(requireActivity(), Observer { handleUiState(it) })
//        ambilUang()
    }

//    private fun handleUiState(it: PemilikProfileState) {
//        when(it){
//            is PemilikProfileState.Loading -> handleLoading(it.state)
//            is PemilikProfileState.ShowToast -> alertGagal(it.message)
//            is PemilikProfileState.SuccessAmbilUang -> alertSuccess("silahkan di tunggu")
//        }
//    }
//
//    private fun alertGagal(message: String){
//        AlertDialog.Builder(requireActivity()).apply {
//            setMessage(message)
//            setPositiveButton("ya"){dialog, _ ->
//                dialog.dismiss()
//            }
//        }.show()
//    }
//
//    private fun alertSuccess(message: String){
//        AlertDialog.Builder(requireActivity()).apply {
//            setMessage(message)
//            setPositiveButton("ya"){dialog, _ ->
//                dialog.dismiss()
//                pemilikProfileViewModel.currentUser("Bearer ${PareUtils.getToken(requireActivity())}")
//            }
//        }.show()
//    }

    @SuppressLint("SetTextI18n")
    private fun handleUser(it: User) {
        //tv_nama.text = it.nama_perusahaan
        tv_email.text = it.email
        //txt_saldo.text = "Saldo \n ${PareUtils.setToIDR(it.saldo!!)}"
    }

    private fun logout(){
        btn_logout.setOnClickListener {
            PareUtils.clearToken(activity!!)
            startActivity(Intent(activity!!, LoginActivity::class.java))
            activity!!.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        ///pemilikProfileViewModel.currentUser("Bearer ${PareUtils.getToken(requireActivity())}")

    }
}