package com.hafiz.pareapp.activiities.penyewa.main_activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hafiz.pareapp.activiities.login_activity.LoginActivity
import com.hafiz.pareapp.R
import com.hafiz.pareapp.fragments.pemilik.NotificationsFragment
import com.hafiz.pareapp.fragments.pemilik.ProfileFragment
import com.hafiz.pareapp.fragments.penyewa.home_fragment.PenyewaHomeFragment
import com.hafiz.pareapp.fragments.penyewa.order_fragment.PenyewaMyOrderFragment
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.penyewa_activity_main.*

class PenyewaMainActivity : AppCompatActivity() {
    companion object { var navStatus = -1 }

    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.penyewa_activity_main)
        actionBar?.setBackgroundDrawable(getDrawable(R.drawable.bg_dark))
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ nav_view.selectedItemId = R.id.navigation_home }

        Thread(Runnable {
            if(PareUtils.getToken(this@PenyewaMainActivity) == null){
                startActivity(Intent(this@PenyewaMainActivity, LoginActivity::class.java))
                finish()
            }
        }).start()
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (navStatus != 0) {
                        fragment =
                            PenyewaHomeFragment()
                        navStatus = 0
                        supportActionBar?.title = "Penyewa"
                    }
                }
                R.id.navigation_order -> {
                    if (navStatus != 1) {
                        fragment = PenyewaMyOrderFragment()
                        navStatus = 1
                    }
                }
                R.id.navigation_notifications -> {
                    if (navStatus != 2) {
                        fragment = NotificationsFragment()
                        navStatus = 2
                    }
                }
                R.id.navigation_profile -> {
                    if (navStatus != 3) {
                        fragment = ProfileFragment()
                        navStatus = 3
                    }
                }
                else -> {
                    navStatus = 0
                    fragment =
                        PenyewaHomeFragment()
                }
            }
            if (fragment == null) {
                navStatus = 0
                fragment =
                    PenyewaHomeFragment()
            }
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.screen_container, fragment!!)
            fragmentTransaction.commit()
            true
        }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}
