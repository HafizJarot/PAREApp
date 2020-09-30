package com.hafiz.pareapp

import android.app.Application
import com.hafiz.pareapp.repositories.*
import com.hafiz.pareapp.ui.login.LoginViewModel
import com.hafiz.pareapp.ui.pemilik.produk.PemilikProdukViewModel
import com.hafiz.pareapp.ui.pemilik.register.PemilikRegisterViewModel
import com.hafiz.pareapp.ui.pemilik.tarik_saldo.TarikSaldoViewModel
import com.hafiz.pareapp.ui.penyewa.order.PenyewaOrderViewModel
import com.hafiz.pareapp.ui.penyewa.register.PenyewaRegisterViewModel
import com.hafiz.pareapp.ui.pemilik.main.home.PemilikHomeViewModel
import com.hafiz.pareapp.ui.pemilik.main.order.PemilikMyOrderViewModel
import com.hafiz.pareapp.ui.pemilik.main.profile.PemilikProfileViewModel
import com.hafiz.pareapp.ui.penyewa.detail_company.PenyewaProductViewModel
import com.hafiz.pareapp.ui.penyewa.detail_product.PenyewaDetailProdukViewModel
import com.hafiz.pareapp.ui.penyewa.main.home.PenyewaHomeViewModel
import com.hafiz.pareapp.ui.penyewa.main.order.PenyewaMyOrderViewModel
import com.hafiz.pareapp.ui.penyewa.main.profile.PenyewaProfileViewModel
import com.hafiz.pareapp.webservices.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(listOf(repositoryModules, viewModelModules, retrofitModule))
        }
    }
}

val retrofitModule = module {
    single { ApiClient.instance() }
    single { FirebaseRepository() }
}

val repositoryModules = module {
    factory { ProdukRepository(get()) }
    factory { UserRepository(get()) }
    factory { OrderRepository(get()) }
    factory { CompanyRepository(get()) }
}

val viewModelModules = module {

    viewModel { PenyewaRegisterViewModel(get()) }
    viewModel { PemilikRegisterViewModel(get(),get()) }

    viewModel { PemilikHomeViewModel(get()) }
    viewModel { PemilikMyOrderViewModel(get()) }
    viewModel { PemilikProdukViewModel(get()) }
    viewModel { PemilikProfileViewModel(get()) }
    viewModel { TarikSaldoViewModel(get()) }

    viewModel { LoginViewModel(get(), get()) }
    viewModel { PenyewaOrderViewModel(get(),get()) }

    viewModel { PenyewaHomeViewModel(get()) }
    viewModel { PenyewaMyOrderViewModel(get()) }
    viewModel { PenyewaProfileViewModel(get()) }
    viewModel { PenyewaProductViewModel(get()) }
    viewModel { PenyewaDetailProdukViewModel(get()) }
}