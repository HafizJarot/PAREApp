package com.hafiz.pareapp.ui.penyewa.order

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.hafiz.pareapp.BuildConfig
import com.hafiz.pareapp.models.CreateOrder
import com.hafiz.pareapp.webservices.ApiClient
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.corekit.models.ItemDetails

class PaymentMidtrans {
    fun initPayment(context: Context, penyewaOrderViewModel: PenyewaOrderViewModel,
                    token : String, createOrder: CreateOrder){
        SdkUIFlowBuilder.init().apply {
            setClientKey(BuildConfig.CLIENT_KEY)
            setContext(context)
            enableLog(true)
            setMerchantBaseUrl("${ApiClient.ENDPOINT}api/snap/")
            setTransactionFinishedCallback{
                it?.let { result ->
                    result.response?.let {
                        when(result.status){
                            TransactionResult.STATUS_FAILED -> {
                                toast(context,"transaction is failed")
                                penyewaOrderViewModel.orderStore(token, createOrder)
                            }
                            TransactionResult.STATUS_PENDING -> {
                                toast(context,"transaction is pending")
                                penyewaOrderViewModel.orderStore(token, createOrder)
                            }
                            TransactionResult.STATUS_SUCCESS -> {
                                toast(context,"transaction is success")
                                penyewaOrderViewModel.orderStore(token, createOrder)
                            }
                            TransactionResult.STATUS_INVALID -> {
                                toast(context,"transaction is invalid")
                                penyewaOrderViewModel.orderStore(token, createOrder)
                            }
                            else -> toast(context,"transaction is invalid")
                        }
                    }
                    if(result.isTransactionCanceled){
                        toast(context,"Transaction is cancelled")
                    }
                    println(result.response.toString())

                } ?: kotlin.run {
                    toast(context,"Response is null")
                }
            }
        }.buildSDK()
    }

    fun showPayment(context: Context, idPenyewa: String, price: Int, lamaSewa : Int, alamat: String){
        val uiKIt = UIKitCustomSetting().apply {
            isSkipCustomerDetailsPages = true
            isShowPaymentStatus = true
        }
        val midtrans = MidtransSDK.getInstance().apply {
            setTransactionRequest(setupTransactionRequest(idPenyewa, price, lamaSewa, alamat ))
            uiKitCustomSetting = uiKIt
        }
        midtrans.startPaymentUiFlow(context)
    }

    private fun setupTransactionRequest(idPenyewa: String, price : Int, qty : Int, alamat : String) : TransactionRequest {
        val request = TransactionRequest(System.currentTimeMillis().toString(), price.toDouble() * qty)
        val items = ArrayList<ItemDetails>()
        items.add(ItemDetails(idPenyewa, price.toDouble(), qty, alamat))
        request.itemDetails = items
        return request
    }

    private fun toast(context: Context,message: String) = Toast.makeText(context,message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}