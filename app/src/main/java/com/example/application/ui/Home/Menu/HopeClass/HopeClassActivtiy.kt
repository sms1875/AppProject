package com.example.application.ui.Home.Menu.HopeClass


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application.R
import com.example.application.databinding.ActivityHopeClassBinding
import com.example.application.network.response.*

class HopeClassActivtiy : AppCompatActivity() {

    private lateinit var binding: ActivityHopeClassBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHopeClassBinding.inflate(layoutInflater)

        if (intent.getStringExtra("myHopeClass")=="myHopeClass"){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.hopeClassFrame, HopeClassForumFragment.newInstant("myHopeClass"))
                .commit()
        }
        else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.hopeClassFrame, HopeClassForumFragment())
                .commit()
        }

        setContentView(binding.root)
    }

    fun myHopeClassFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.hopeClassFrame,HopeClassForumFragment.newInstant("myHopeClass"))
            .addToBackStack(null)
            .commit()
    }

    fun hopeClassPostFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.hopeClassFrame,HopeClassPostFragment())
            .addToBackStack(null)
            .commit()
    }

    fun hopeClasspageFragment(hopeClassPage: hopeClassPage,topicId:String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.hopeClassFrame,HopeClassPageFragment.newInstant(hopeClassPage,topicId))
            .addToBackStack(null)
            .commit()
    }
}
