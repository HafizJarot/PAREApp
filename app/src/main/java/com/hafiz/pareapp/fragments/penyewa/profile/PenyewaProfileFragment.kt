package com.hafiz.pareapp.fragments.penyewa.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.login.LoginActivity
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.penyewa_fragment_profile.*

class PenyewaProfileFragment : Fragment(R.layout.penyewa_fragment_profile){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout()
    }

    private fun logout(){
        btn_logout.setOnClickListener {
            PareUtils.clearToken(activity!!)
            startActivity(Intent(activity!!, LoginActivity::class.java))
            activity!!.finish()
        }
    }
}