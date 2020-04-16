package com.hafiz.pareapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hafiz.pareapp.fragments.DashboardFragment
import com.hafiz.pareapp.fragments.NotificationsFragment
import com.hafiz.pareapp.fragments.OrderFragment
import com.hafiz.pareapp.fragments.ProfileFragment
import com.hafiz.pareapp.utils.PareUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{ var navStatus = -1 }
    private var fragment : Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ nav_view.selectedItemId = R.id.navigation_dashboard }
        Thread(Runnable {
            if(PareUtils.getToken(this@MainActivity) == null){
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }).start()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_dashboard -> {
                if(navStatus != 0){
                    fragment = DashboardFragment()
                    navStatus = 0
                }
            }
            R.id.navigation_order -> {
                if(navStatus != 1){
                    fragment = OrderFragment()
                    navStatus = 1
                }
            }
            R.id.navigation_notifications -> {
                if(navStatus != 2){
                    fragment = NotificationsFragment()
                    navStatus = 2
                }
            }
            R.id.navigation_profile -> {
                if(navStatus != 3){
                    fragment = ProfileFragment()
                    navStatus = 3
                }
            }
            else -> {
                navStatus = 0
                fragment = DashboardFragment()
            }
        }
        if(fragment == null){
            navStatus = 0
            fragment = DashboardFragment()
        }
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.screen_container, fragment!!)
        fragmentTransaction.commit()
        true
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}
