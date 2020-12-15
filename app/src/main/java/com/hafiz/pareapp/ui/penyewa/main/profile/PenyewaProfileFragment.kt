package com.hafiz.pareapp.ui.penyewa.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hafiz.pareapp.R
import com.hafiz.pareapp.ui.login.LoginActivity
import com.hafiz.pareapp.models.User
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.extensions.gone
import com.hafiz.pareapp.utils.extensions.showToast
import com.hafiz.pareapp.utils.extensions.visible
import kotlinx.android.synthetic.main.penyewa_fragment_profile.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PenyewaProfileFragment : Fragment(R.layout.penyewa_fragment_profile){

    private val penyewaProfileViewModel : PenyewaProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout()
        observe()
    }


    private fun observe(){
        observeState()
        observeCurrentUser()
    }

    private fun observeCurrentUser() = penyewaProfileViewModel.listenToCurrentUser().observe(requireActivity(), Observer { handleUser(it) })
    private fun observeState() = penyewaProfileViewModel.listenToState().observe(requireActivity(), Observer { handleUiState(it) })

    private fun handleUiState(penyewaProfileState: PenyewaProfileState?) {
        penyewaProfileState?.let {
            when(it){
                is PenyewaProfileState.Loading -> handleLoading(it.state)
                is PenyewaProfileState.ShowToast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        if (b) requireView().loading.visible() else requireView().loading.gone()
    }

    private fun handleUser(it: User) {
        requireView().tv_nama.text = it.penyewa?.nama
        requireView().tv_email.text = it.email
    }

    private fun logout(){
        requireView().btn_logout.setOnClickListener {
            PareUtils.clearToken(requireActivity())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun currentUser() = penyewaProfileViewModel.currentUser("Bearer ${PareUtils.getToken(requireActivity())}")

    override fun onResume() {
        super.onResume()
        currentUser()
    }
}