package com.hafiz.pareapp.fragments.pemilik.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.login.LoginActivity
import com.hafiz.pareapp.fragments.penyewa.profile.PenyewaProfileViewModel
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.penyewa_fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PemilikProfileFragment : Fragment(R.layout.pemilik_fragment_profile){
    private val pemilikProfileViewModel : PemilikProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout()
        pemilikProfileViewModel.listenToCurrentUser().observe(requireActivity(), Observer { handleUser(it) })
    }

    private fun handleUser(it: User) {
        tv_nama.text = it.nama_perusahaan
        tv_email.text = it.email
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
        pemilikProfileViewModel.currentUser("Bearer ${PareUtils.getToken(requireActivity())}")

    }
}