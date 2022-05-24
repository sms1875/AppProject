package com.example.application.ui.Home.Menu.Place

import ImageSliderAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.application.*
import com.example.application.databinding.ActivityPlaceDetailBinding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import android.widget.LinearLayout

import androidx.viewpager2.widget.ViewPager2


import androidx.core.content.ContextCompat

import android.view.ViewGroup
import android.widget.ImageView

import com.example.application.network.response.*
import com.example.application.network.service.*
class PlaceDetailActivity : AppCompatActivity(){

    private lateinit var binding: ActivityPlaceDetailBinding


    lateinit var sliderViewPager: ViewPager2
    lateinit var layoutIndicator: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)

        val intent: Intent = getIntent()
        val placeData:placeData= intent.getParcelableExtra("placeInfo")!!

        val TOKEN=SharedPreferences.prefs.getString("key","key")
        val reservationPlaceService = ServiceGenerator.createService(reservationPlaceInterface::class.java,TOKEN)
        val cancelRegisterPlaceService = ServiceGenerator.createService(cancelRegisterPlaceInterface::class.java,TOKEN)
        /*******************************뷰페이저*********************************/
        sliderViewPager = binding.placeImageViewpager
        layoutIndicator = binding.layoutIndicators


        val images = placeData.placeImage.substring(0, placeData.placeImage.length - 1).split(",")
        val array: Array<String> = images.toTypedArray()

        sliderViewPager.offscreenPageLimit = 1
        sliderViewPager.adapter = ImageSliderAdapter(this, array)
        sliderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        setupIndicators(array.size)//마지막 ,"" 제외
        /*******************************뷰페이저*********************************/
        binding.txtPlaceName.text=placeData.name
        binding.txtDateTime.text=String.format("시작일 : %s \n 종료일 : %s ",(placeData.start_time),(placeData.end_time))
        binding.txtPlaceInfo.text=placeData.detail
        binding.txtPlacePrice.text=placeData.cost
        binding.txtPlaceType.text=placeData.type

        binding.btnReservation.setOnClickListener {
            reservationPlaceService.reservationPlace(placeData._id).enqueue(object :Callback<resultResponse> {
                override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                    val result = response.body()
                    Log.d("ee", result?.result.toString())
                    when (result?.result) {
                        "success" -> {
                            val toast = Toast.makeText(applicationContext, binding.txtPlaceName.text.toString()+" 예약이 완료되었습니다", Toast.LENGTH_SHORT)
                            toast.show()

                            finish()
                        }//로그인성공
                        "fail" -> {
                            val toast = Toast.makeText(applicationContext, "수강신청에 실패했습니다", Toast.LENGTH_SHORT)
                            toast.show()
                        }//로그인실패
                        "internal" -> {
                            val toast = Toast.makeText(applicationContext, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT)
                            toast.show()
                        }//서버오류
                    }
                }
                override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                    Log.d("fail", "$t")
                }
            })
        }//수강신청 버튼



        if(SharedPreferences.prefs.getString("userType","")=="강사"){
            binding!!.btnReservation2.visibility=View.VISIBLE
            binding!!.btnReservation2.isEnabled=true
        }

        binding.btnReservation2.setOnClickListener {
            cancelRegisterPlaceService.reservationPlace(placeData._id).enqueue(object :Callback<resultResponse> {
                override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                    val result = response.body()
                    Log.d("ee", result?.result.toString())
                    when (result?.result) {
                        "success" -> {
                            val toast = Toast.makeText(applicationContext, binding.txtPlaceName.text.toString()+" 예약이 완료되었습니다", Toast.LENGTH_SHORT)
                            toast.show()

                            finish()
                        }//로그인성공
                        "fail" -> {
                            val toast = Toast.makeText(applicationContext, "수강신청에 실패했습니다", Toast.LENGTH_SHORT)
                            toast.show()
                        }//로그인실패
                        "internal" -> {
                            val toast = Toast.makeText(applicationContext, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT)
                            toast.show()
                        }//서버오류
                    }
                }
                override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                    Log.d("fail", "$t")
                }
            })
        }//수강신청 버튼


        setContentView(binding.root)
    }

    private fun setupIndicators(count: Int) {
        val indicators: Array<ImageView?> = arrayOfNulls<ImageView>(count)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 8, 16, 8)
        for (i in indicators.indices) {
            indicators[i] = ImageView(this)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_indicator_inactive
                )
            )
            indicators[i]?.setLayoutParams(params)
            layoutIndicator.addView(indicators[i])
        }
        setCurrentIndicator(0)
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = layoutIndicator.childCount
        for (i in 0 until childCount) {
            val imageView: ImageView = layoutIndicator.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                    )
                )
            }
        }
    }
}


