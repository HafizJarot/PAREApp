package com.hafiz.pareapp.ui.penyewa.main.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.models.Produk
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.extensions.gone
import com.hafiz.pareapp.utils.extensions.visible
import kotlinx.android.synthetic.main.penyewa_fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PenyewaHomeFragment :Fragment(R.layout.penyewa_fragment_home){

    private val penyewaHomeViewModel : PenyewaHomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView(){
        recycler_view.apply {
            adapter = PenyewaHomeAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun observe() {
        observeState()
        observeCompanies()
    }

    private fun observeState() = penyewaHomeViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeCompanies() = penyewaHomeViewModel.listenToCompanies().observe(viewLifecycleOwner, Observer { handleProducts(it) })
    private fun fetchCompanies() = penyewaHomeViewModel.fetchCompanies("Bearer ${PareUtils.getToken(requireActivity())}")

    private fun handleProducts(list: List<Pemilik>?) {
        list?.let {
            recycler_view.adapter?.let {a ->
                if (a is PenyewaHomeAdapter){
                    a.changelist(it)
                }
            }
        }
    }

    private fun handleUiState(homeState: PenyewaHomeState?) {
        homeState?.let {
            when(it){
                is PenyewaHomeState.Loading -> handleLoading(it.state)
                is PenyewaHomeState.ShowToast -> toast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) = if (state) loading.visible() else loading.gone()
    private fun toast(message : String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        fetchCompanies()
    }
}