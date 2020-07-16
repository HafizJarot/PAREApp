package com.hafiz.pareapp

import android.app.Application
import com.hafiz.pareapp.activiities.login.LoginViewModel
import com.hafiz.pareapp.activiities.pemilik.produk.PemilikProdukViewModel
import com.hafiz.pareapp.activiities.pemilik.register.PemilikRegisterViewModel
import com.hafiz.pareapp.activiities.penyewa.order.PenyewaOrderViewModel
import com.hafiz.pareapp.activiities.penyewa.produk.PenyewaProdukViewModel
import com.hafiz.pareapp.activiities.penyewa.register.PenyewaRegisterViewModel
import com.hafiz.pareapp.fragments.pemilik.home.PemilikHomeViewModel
import com.hafiz.pareapp.fragments.pemilik.order.PemilikMyOrderViewModel
import com.hafiz.pareapp.fragments.penyewa.home.PenyewaHomeViewModel
import com.hafiz.pareapp.fragments.penyewa.order.PenyewaMyOrderViewModel
import com.hafiz.pareapp.repositories.FirebaseRepository
import com.hafiz.pareapp.repositories.OrderRepository
import com.hafiz.pareapp.repositories.ProdukRepository
import com.hafiz.pareapp.repositories.UserRepository
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
}

val viewModelModules = module {

    viewModel { PenyewaRegisterViewModel(get()) }
    viewModel { PemilikRegisterViewModel(get(),get()) }

    viewModel { PemilikHomeViewModel(get()) }
    viewModel { PemilikProdukViewModel(get()) }
    viewModel { PemilikMyOrderViewModel(get()) }


    viewModel { LoginViewModel(get()) }
    viewModel { PenyewaOrderViewModel(get(),get()) }
    viewModel { PenyewaProdukViewModel(get()) }

    viewModel { PenyewaHomeViewModel(get()) }
    viewModel { PenyewaMyOrderViewModel(get()) }
}