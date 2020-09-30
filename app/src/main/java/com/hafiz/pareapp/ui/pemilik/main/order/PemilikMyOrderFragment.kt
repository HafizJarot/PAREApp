package com.hafiz.pareapp.ui.pemilik.main.order

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.hafiz.pareapp.R
import com.hafiz.pareapp.adapters.pemilik.PemilikMyOrderAdapter
import com.hafiz.pareapp.utils.extensions.gone
import com.hafiz.pareapp.utils.extensions.visible
import com.hafiz.pareapp.models.Order
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.pemilik_fragment_my_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PemilikMyOrderFragment : Fragment(R.layout.pemilik_fragment_my_order){

    private val pemilikMyOrderViewModel : PemilikMyOrderViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        pemilikMyOrderViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUI(it) })
        pemilikMyOrderViewModel.listenToOrders().observe(viewLifecycleOwner, Observer { handleData(it) })
    }

    private fun handleData(it: List<Order>) {
        rv_pemilik_my_order.adapter?.let { adapter ->
            if (adapter is PemilikMyOrderAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun handleUI(it: PemilikMyOrderState) {
        when(it){
            is PemilikMyOrderState.IsLoading -> {
                if (it.state){
                    pb_pemilik_my_order.visible()
                }else{
                    pb_pemilik_my_order.gone()
                }
            }
            is PemilikMyOrderState.ShowToast -> toast(it.message)
        }
    }

    private fun setupUI() {
        rv_pemilik_my_order.apply {
            adapter = PemilikMyOrderAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(),2)
        }
    }

    private fun toast(message : String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        pemilikMyOrderViewModel.getMyOrders("Bearer ${PareUtils.getToken(requireActivity())}")
    }
}