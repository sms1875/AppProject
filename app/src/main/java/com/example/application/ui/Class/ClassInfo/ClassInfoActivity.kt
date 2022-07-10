package com.example.application.ui.Class.ClassInfo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.example.application.*
import com.example.application.databinding.ActivityClassInfoBinding
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.application.network.response.*
import com.example.application.network.service.*
import com.example.application.ui.Class.ClassInfo.Info.ClassInfoTabFragment
import com.example.application.ui.Class.ClassInfo.QnA.ClassQnATab
import com.example.application.ui.Class.ClassInfo.Review.ClassReviewTabFragment


class ClassInfoActivity : AppCompatActivity() {

    lateinit var classInfo:classDetailInfo
    lateinit var classid:String

    val fragmentList = listOf(ClassInfoTabFragment(), ClassReviewTabFragment(), ClassQnATab())

    private lateinit var binding: ActivityClassInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassInfoBinding.inflate(layoutInflater)

        classid= intent.getStringExtra("classID").toString() //POST요청에 사용할 classid
        val myClass= intent.getStringExtra("myClass").toString() //POST요청에 사용할 classid




        val getClassDetailService = ServiceGenerator.createService(getClassDetailInterface::class.java)

        val TOKEN= SharedPreferences.prefs.getString("key","key")
        val singupClassService = ServiceGenerator.createService(singupClassInterface::class.java,TOKEN)
        val cancelReservationClassService = ServiceGenerator.createService(
            cancelReservationClassInterface::class.java,TOKEN)



        getClassDetailService.getClassDetail(classid).enqueue(object : Callback<classDetailInfo> {
            override fun onResponse(call: Call<classDetailInfo>, response: Response<classDetailInfo>) {
                Log.d("ㅁㅁㅁㅁㅁㅁㅁㅁ", " : ${response.body()}")
                classInfo = response.body()!!
                val uri = classInfo.classImage.split(",")
                Glide
                    .with(this@ClassInfoActivity)
                    .load(serverAddress+"/class/getImg/" + uri[0])
                    .override(100, 100)
                    .into(binding.ivClassImage)
                binding.txtClassName.text=classInfo.className

                val pagerAdatper = FragmentPagerAdapter(fragmentList, this@ClassInfoActivity,classInfo)//페이지 어댑터 설정
                with(binding) {
                    viewPager.adapter = pagerAdatper
                    viewPager.setUserInputEnabled(false)//좌우 스와이프 막기

                    val titles = listOf("수업 정보", "리뷰", "QnA게시판")
                    TabLayoutMediator(binding.classInfoTabLayout, binding.viewPager) { tab, position ->
                        tab.text = titles.get(position)
                    }.attach()
                }
                //탭레이아웃 설정
            }
            override fun onFailure(call: Call<classDetailInfo>, t: Throwable) {
                Log.d("detail:", "실패 : $t")
                val toast = Toast.makeText(applicationContext, "서버와 연결이 실패했습니다", Toast.LENGTH_SHORT)
                toast.show()
            }
        })


        binding.btnSingupClass.setOnClickListener {
            singupClassService.singupClass(classid).enqueue(object : Callback<resultResponse> {
                override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                    val result = response.body()
                    Log.d("ee", result?.result.toString())
                    when (result?.result) {
                        "success" -> {
                            val toast = Toast.makeText(applicationContext, binding.txtClassName.text.toString()+" 신청이 완료되었습니다", Toast.LENGTH_SHORT)
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
        }

        if (myClass=="myClass"){
            binding.btnSingupClass.text="수강 취소"
            binding.btnSingupClass.setOnClickListener {
                cancelReservationClassService.cancelReservation(classid).enqueue(object : Callback<resultResponse> {
                    override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                        val result = response.body()
                        when (result?.result) {
                            "success" -> {
                                val toast = Toast.makeText(applicationContext, binding.txtClassName.text.toString()+" 신청이 완료되었습니다", Toast.LENGTH_SHORT)
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
            }
        }


        /*if(SharedPreferences.prefs.getString("userType","")=="강사"){
            binding!!.btnSingupClass2.visibility= View.VISIBLE
            binding!!.btnSingupClass2.isEnabled=true
        }*/



        /*classInfo=classDetailInfo("name","tutor","category","4000","1",
            "18564189616gregag\n\n\n\n\n\n\naethgetrahtaerhaertrthartharth","1","10-10","10-99",
            "05:10","07:30","충북대학교","4.7","200")*/


        setContentView(binding.root)
    }


    class FragmentPagerAdapter(val fragmentList:List<Fragment>, fragmentActivity: FragmentActivity,var classData:classDetailInfo )
        : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount() = fragmentList.size
        //override fun createFragment(position: Int)= fragmentList.get(position)
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ClassInfoTabFragment.newInstant(classData)
                1 -> ClassReviewTabFragment.newInstant(classData)
                2 -> ClassQnATab.newInstant(classData)
                else -> fragmentList.get(position)
            }
        }
    }//프레그먼트 어댑터

}




