package com.hafiz.pareapp.fragments.pemilik.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafiz.pareapp.R
import com.hafiz.pareapp.activiities.pemilik.produk.PemilikProdukActivity
import com.hafiz.pareapp.adapters.pemilik.PemilikProdukAdapter
import com.hafiz.pareapp.extensions.gone
import com.hafiz.pareapp.extensions.visible
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.pemilik_fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PemilikHomeFragment :Fragment(R.layout.pemilik_fragment_home){

    private val pemilikHomeViewModel : PemilikHomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_home.apply {
            adapter = PemilikProdukAdapter(mutableListOf(), activity!!)
            layoutManager = LinearLayoutManager(activity!!)
        }

        pemilikHomeViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleui(it) })
        pemilikHomeViewModel.listenToProduks().observe(viewLifecycleOwner, Observer {
            rv_home.adapter?.let {adapter ->
                if (adapter is PemilikProdukAdapter){
                    adapter.changelist(it)
                }
            }
        })
        fab_create.setOnClickListener { startActivity(Intent(activity, PemilikProdukActivity::class.java)) }
    }

    private fun handleui(it : PemilikHomeState){
        when(it){
            is PemilikHomeState.IsLoading -> {
                if (it.state){
                    pb_home.isIndeterminate = true
                    pb_home.visible()
                }else{
                    pb_home.isIndeterminate = false
                    pb_home.gone()
                }
            }
            is PemilikHomeState.ShowToast -> toast(it.message)
        }
    }

    override fun onResume() {
        super.onResume()
        pemilikHomeViewModel.getMyProduks("Bearer ${PareUtils.getToken(activity!!)}")
    }

    private fun toast(message : String) = Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
}