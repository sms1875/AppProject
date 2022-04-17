package com.example.application


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.application.Class.ClassSearchFragment
import com.example.application.Home.*
import com.example.application.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.bottomNav.run { setOnItemSelectedListener() {
            val fm = supportFragmentManager
            val transaction: FragmentTransaction = fm.beginTransaction()
            when(it.itemId) {
                R.id.nav_menu -> {
                    fm.popBackStackImmediate("menu", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    transaction.replace(R.id.frame_layout,MenuFragment(), "menu")
                    transaction.addToBackStack("menu")

                }
                R.id.nav_home -> {
                    fm.popBackStackImmediate("home", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    transaction.replace(R.id.frame_layout,HomeFragment(), "home")
                    transaction.addToBackStack("home")
                }
                R.id.nav_class -> {
                    fm.popBackStackImmediate("class", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    transaction.replace(R.id.frame_layout,ClassFragment(), "class")
                    transaction.addToBackStack("class")
                }
                R.id.nav_class_search -> {
                    fm.popBackStackImmediate("classSearch", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    transaction.replace(R.id.frame_layout,ClassSearchFragment(), "classSearch")
                    transaction.addToBackStack("classSearch")
                }
                R.id.nav_mypage -> {
                    fm.popBackStackImmediate("mypage", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    transaction.replace(R.id.frame_layout,MypageFragment(), "mypage")
                    transaction.addToBackStack("mypage")
                }
            }
            transaction.commit()
            true
        }
            selectedItemId = R.id.nav_home
        }
        setContentView(binding.root)
    }

    /******************************/
    private fun updateBottomMenu(navigation: BottomNavigationView) {
        val tag1: Fragment? = supportFragmentManager.findFragmentByTag("home")
        val tag2: Fragment? = supportFragmentManager.findFragmentByTag("class")
        val tag3: Fragment? = supportFragmentManager.findFragmentByTag("mypage")
        val tag4: Fragment? = supportFragmentManager.findFragmentByTag("classSearch")
        val tag5: Fragment? = supportFragmentManager.findFragmentByTag("menu")

        if(tag1 != null && tag1.isVisible) {navigation.menu.findItem(R.id.nav_home).isChecked = true }
        if(tag2 != null && tag2.isVisible) {navigation.menu.findItem(R.id.nav_class).isChecked = true }
        if(tag3 != null && tag3.isVisible) {navigation.menu.findItem(R.id.nav_mypage).isChecked = true }
        if(tag4 != null && tag4.isVisible) {navigation.menu.findItem(R.id.nav_class_search).isChecked = true }
        if(tag5 != null && tag5.isVisible) {navigation.menu.findItem(R.id.nav_menu).isChecked = true }
    }
    /******************************/
    private var FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1) {
            val tempTime = System.currentTimeMillis()
            val intervalTime = tempTime - backPressedTime
            if (!(0 > intervalTime || FINISH_INTERVAL_TIME < intervalTime)) {
                finishAffinity()
                System.runFinalization()
                System.exit(0)
            } else {
                backPressedTime = tempTime
                Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return
            }
        }
        super.onBackPressed()
        val bnv = findViewById<View>(R.id.bottom_nav) as BottomNavigationView
        updateBottomMenu(bnv)
    }
    //네비게이션 백스택,종료 설정

    fun openClassFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, com.example.application.Home.openClassFragment)
            .addToBackStack("null")
            .commit()
    }
    fun ReservationPlaceFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, com.example.application.Home.reservationPlaceFragment)
            .addToBackStack("null")
            .commit()
    }
    fun MyRegisterPlaceFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, com.example.application.Home.myRegisterPlaceFragment)
            .addToBackStack("null")
            .commit()
    }
    fun SignupClassFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, com.example.application.Home.singupClassFragment)
            .addToBackStack("null")
            .commit()
    }
    fun InstructorRegisterFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, instructorRegisterFragment)
            .addToBackStack("null")
            .commit()
    }
    fun placeFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, com.example.application.Home.PlaceFragment())
            .addToBackStack("null")
            .commit()
    }
}

