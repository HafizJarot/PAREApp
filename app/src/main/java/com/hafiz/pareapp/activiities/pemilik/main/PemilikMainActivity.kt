package com.hafiz.pareapp.activiities.pemilik.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hafiz.pareapp.activiities.login.LoginActivity
import com.hafiz.pareapp.R
import com.hafiz.pareapp.fragments.pemilik.home.PemilikHomeFragment
import com.hafiz.pareapp.fragments.pemilik.NotificationsFragment
import com.hafiz.pareapp.fragments.pemilik.order.PemilikMyOrderFragment
import com.hafiz.pareapp.fragments.pemilik.profile.PemilikProfileFragment
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.pemilik_activity_main.*

class PemilikMainActivity : AppCompatActivity() {
    companion object { var navStatus = -1 }
    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pemilik_activity_main)
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ nav_view.selectedItemId = R.id.navigation_home }
        Thread(Runnable {
            if(PareUtils.getToken(this@PemilikMainActivity) == null){
                startActivity(Intent(this@PemilikMainActivity, LoginActivity::class.java))
                finish()
            }
        }).start()
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (navStatus != 0) {
                        fragment = PemilikHomeFragment()
                        supportActionBar?.title = "Pemilik"
                        navStatus = 0
                    }
                }
                R.id.navigation_order -> {
                    if (navStatus != 1) {
                        fragment = PemilikMyOrderFragment()
                        navStatus = 1
                    }
                }
//                R.id.navigation_notifications -> {
//                    if (navStatus != 2) {
//                        fragment = NotificationsFragment()
//                        navStatus = 2
//                    }
//                }
                R.id.navigation_profile -> {
                    if (navStatus != 3) {
                        fragment =
                            PemilikProfileFragment()
                        navStatus = 3
                    }
                }
                else -> {
                    navStatus = 0
                    fragment =
                        PemilikHomeFragment()
                }
            }
            if (fragment == null) {
                navStatus = 0
                fragment =
                    PemilikHomeFragment()
            }
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.screen_container, fragment!!)
            fragmentTransaction.commit()
            true
        }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}
