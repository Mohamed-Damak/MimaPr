package com.example.myapplication
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*


open  class MainActivity : AppCompatActivity() {
    private val dashbordFragment=DashbordFragment()
    private val settingFragment=SettingFragment()
    private val infoFragment=InfoFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF5151")))
replaceFragment(dashbordFragment)
        bottom_navigation.setOnNavigationItemReselectedListener{
            when(it.itemId){
R.id.ic_dashbord->replaceFragment(dashbordFragment)
                R.id.ic_setting->replaceFragment(settingFragment)
                R.id.ic_info->replaceFragment(infoFragment)

            }
        }
    }


private fun replaceFragment(fragment: Fragment){
    if (fragment!=null) {
    val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()
    }
    }
}
