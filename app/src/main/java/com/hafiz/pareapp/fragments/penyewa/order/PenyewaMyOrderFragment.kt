package com.hafiz.pareapp.fragments.penyewa.order

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafiz.pareapp.R
import com.hafiz.pareapp.adapters.penyewa.PenyewaMyOrderAdapter
import com.hafiz.pareapp.extensions.gone
import com.hafiz.pareapp.extensions.visible
import com.hafiz.pareapp.models.Order
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.penyewa_fragment_my_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PenyewaMyOrderFragment : Fragment(R.layout.penyewa_fragment_my_order){

    private val penyewaMyOrderViewModel : PenyewaMyOrderViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_penyewa_my_order.apply {
            adapter = PenyewaMyOrderAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
        penyewaMyOrderViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUI(it) })
        penyewaMyOrderViewModel.listenToOrders().observe(viewLifecycleOwner, Observer { handleOrder(it) })
    }

    private fun handleUI(it : PenyewaMyOrderSate){
        when(it){
            is PenyewaMyOrderSate.IsLoading -> {
                if (it.state){
                    pb_penyewa_my_order.visible()
                }else{
                    pb_penyewa_my_order.gone()
                }
            }
            is PenyewaMyOrderSate.ShwoToast -> toast(it.message)
        }
    }

    private fun handleOrder(it : List<Order>){
        rv_penyewa_my_order.adapter?.let {adapter ->
            if (adapter is PenyewaMyOrderAdapter){
                adapter.changelist(it)
            }
        }
    }


    private fun toast(message : String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    override fun onResume() {
        super.onResume()
        penyewaMyOrderViewModel.getPenyewaMyOrders("Bearer ${PareUtils.getToken(requireActivity())}")
    }
}