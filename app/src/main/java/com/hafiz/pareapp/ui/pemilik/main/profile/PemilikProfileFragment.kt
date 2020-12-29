package com.hafiz.pareapp.ui.pemilik.main.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.ui.login.LoginActivity
import com.hafiz.pareapp.ui.pemilik.tarik_saldo.TarikSaldoActivity
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.pemilik_fragment_profile.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PemilikProfileFragment : Fragment(R.layout.pemilik_fragment_profile){
    private val pemilikProfileViewModel : PemilikProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout()
        pemilikProfileViewModel.listenToCurrentUser().observe(requireActivity(), Observer { handleUser(it) })
        requireView().txt_tarik_saldo.setOnClickListener { startActivity(Intent(requireActivity(), TarikSaldoActivity::class.java)) }
        pemilikProfileViewModel.listenToState().observer(requireActivity(), Observer { handleUiState(it) })
//        ambilUang()
    }

    private fun handleUiState(it: PemilikProfileState) {
        when(it){
            is PemilikProfileState.Loading -> handleLoading(it.state)
            is PemilikProfileState.ShowToast -> alertGagal(it.message)
            is PemilikProfileState.SuccessAmbilUang -> alertSuccess("silahkan di tunggu")
        }
    }

    private fun handleLoading(b: Boolean) {
        //if (b) else
    }

    //
    private fun alertGagal(message: String){
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }
//
    private fun alertSuccess(message: String){
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
                pemilikProfileViewModel.currentUser("Bearer ${PareUtils.getToken(requireActivity())}")
            }
        }.show()
    }

    @SuppressLint("SetTextI18n")
    private fun handleUser(it: User) {
        requireView().tv_nama.text = it.pemilik?.nama_perusahaan
        requireView().tv_email.text = it.email
        requireView().txt_saldo.text = "Saldo \n ${PareUtils.setToIDR(it.pemilik?.saldo!!)}"
    }

    private fun logout() {
        requireView().btn_logout.setOnClickListener {
            PareUtils.clearToken(activity!!)
            startActivity(Intent(activity!!, LoginActivity::class.java))
            activity!!.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        pemilikProfileViewModel.currentUser("Bearer ${PareUtils.getToken(requireActivity())}")

    }
}