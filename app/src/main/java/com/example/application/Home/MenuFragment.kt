package com.example.application.Home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.application.Home.Menu.HopeClass.HopeClassActivtiy
import com.example.application.Home.Menu.Place.PlaceRegisterActivity
import com.example.application.MainActivity
import com.example.application.SharedPreferences
import com.example.application.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(SharedPreferences.prefs.getString("userType","")=="강사"){
            binding.btnPlaceMenu.visibility=View.VISIBLE
            binding.btnPlaceMenu.isEnabled=true
        }

        binding.btnPlaceMenu.setOnClickListener {
            binding.placeMenuLayout.visibility=View.VISIBLE
            binding.placeMenuLayout.isEnabled=true

            binding.hopeClassMenuLayout.visibility=View.GONE
            binding.hopeClassMenuLayout.isEnabled=false
        }

        binding.btnHopeClassMenu.setOnClickListener {
            binding.hopeClassMenuLayout.visibility=View.VISIBLE
            binding.hopeClassMenuLayout.isEnabled=true

            binding.placeMenuLayout.visibility=View.GONE
            binding.placeMenuLayout.isEnabled=false
        }

        binding.btnHopeClassForum.setOnClickListener {
            val intent_HopeClass= Intent(context, HopeClassActivtiy::class.java)
            startActivity(intent_HopeClass)
        }

        binding.btnMyHopeClass.setOnClickListener {
            val intent_HopeClass= Intent(context, HopeClassActivtiy::class.java)
            intent_HopeClass.putExtra("myHopeClass","myHopeClass")
            startActivity(intent_HopeClass)
        }

        binding.btnPlaceRentalList.setOnClickListener {
            (activity as MainActivity).ReservationPlaceFragment()
        }

        binding.btnPlaceRegister.setOnClickListener {
            val intent_placeRegister= Intent(context, PlaceRegisterActivity::class.java)
            startActivity(intent_placeRegister)
        }

        binding.btnPlaceList.setOnClickListener {
            (activity as MainActivity).placeFragment()
        }

        binding.btnMyRegisterPlaceList.setOnClickListener {
            (activity as MainActivity).MyRegisterPlaceFragment()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

