package com.hafiz.pareapp.ui.penyewa.main.home

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.hafiz.pareapp.R
import com.hafiz.pareapp.models.Kecamatan
import com.hafiz.pareapp.models.Pemilik
import com.hafiz.pareapp.utils.PareUtils
import com.hafiz.pareapp.utils.extensions.gone
import com.hafiz.pareapp.utils.extensions.showToast
import com.hafiz.pareapp.utils.extensions.visible
import kotlinx.android.synthetic.main.penyewa_fragment_home.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PenyewaHomeFragment :Fragment(R.layout.penyewa_fragment_home){

    private val penyewaHomeViewModel : PenyewaHomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView(){
        requireView().recycler_view.apply {
            adapter = PenyewaHomeAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun observe() {
        observeState()
        observeCompanies()
        observeKecamatan()
    }

    private fun observeKecamatan() = penyewaHomeViewModel.listenToKecamatan().observe(viewLifecycleOwner, Observer { handleKecamatan(it) })

    private fun handleKecamatan(list: List<Kecamatan>?) {
        list?.let {kcm ->
            val kecamatan = kcm.distinctBy { k -> k.kecamatan }
            val kec : MutableList<String> = mutableListOf("Semua")
            kecamatan.map { kec.add(it.kecamatan!!) }
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, kec)
                .apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            requireView().spinner_kecamatan.adapter = adapter
            requireView().spinner_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (kec[position] == "Semua") {
                        fetchCompanies()
                    }else{
                        val k = kcm.find { k -> k.kecamatan == kec[position]  }
                        searchCompanies(k!!.id.toString())
                    }
                }
            }
        }
    }

    private fun observeState() = penyewaHomeViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeCompanies() = penyewaHomeViewModel.listenToCompanies().observe(viewLifecycleOwner, Observer { handleProducts(it) })
    private fun fetchCompanies() = penyewaHomeViewModel.fetchCompanies("Bearer ${PareUtils.getToken(requireActivity())}")
    private fun fetchKecamatan() = penyewaHomeViewModel.fetchKecamatan()
    private fun searchCompanies(id_kecamatan : String){ penyewaHomeViewModel.searchCompanies("Bearer ${PareUtils.getToken(requireActivity())}", id_kecamatan) }

    private fun handleProducts(list: List<Pemilik>?) {
        list?.let {
            requireView().recycler_view.adapter?.let {a ->
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
                is PenyewaHomeState.ShowToast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) = if (state) requireView().loading.visible() else requireView().loading.gone()

    override fun onResume() {
        super.onResume()
        fetchCompanies()
        fetchKecamatan()
    }
}