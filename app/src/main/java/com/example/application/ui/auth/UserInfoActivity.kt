package com.example.application.ui.auth


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.application.*
import com.example.application.databinding.ActivityUserInfoBinding
import com.example.application.network.service.*
import com.example.application.network.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding
    var userdata:GetMyInfoResponse?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val TOKEN = SharedPreferences.prefs.getString("key", "token")
        val getMyInfoService = ServiceGenerator.createService(getMyInfoInterface::class.java, TOKEN)
        val updateInfoService = ServiceGenerator.createService(updateInfoInterface::class.java, TOKEN)


        //**********************************************************************//
        with(getMyInfoService) {
            getMyInfo().enqueue(object : Callback<GetMyInfoResponse> {
                override fun onResponse(call: Call<GetMyInfoResponse>, response: Response<GetMyInfoResponse>) {
                    val result = response.body()
                    when (result?.result) {
                        "success" -> {
                            userdata=result

                        }
                        "fail" -> {
                            val toast = android.widget.Toast.makeText(applicationContext, "등록에 실패했습니다", android.widget.Toast.LENGTH_SHORT)
                            toast.show()
                        }//로그인실패
                        "internal" -> {
                            val toast = android.widget.Toast.makeText(applicationContext, "잠시후 다시 시도해주세요", android.widget.Toast.LENGTH_SHORT)
                            toast.show()
                        }//서버오류

                    }
                    binding.editPhoneNum.setText(userdata?.tel)
                    binding.editName.setText(userdata?.username)
                }
                override fun onFailure(call: Call<GetMyInfoResponse>, t: Throwable) {
                    android.util.Log.e("실패", "${t}")
                }
            })
        }

        //**********************************************************************//



        binding.btnUpdateInfo.setOnClickListener {
            var usertel = binding.editPhoneNum.text.toString()
            var userName=binding.editName.text.toString()


                updateInfoService.updateInfo(userName,usertel).enqueue(object : Callback<updateInfoResponse> {
                    override fun onResponse(call: Call<updateInfoResponse>, response: Response<updateInfoResponse>) {
                        val result = response.body()
                        when (result?.result) {
                            "success" -> {
                                SharedPreferences.prefs.setString("userType",result.usertype)
                                val toast = Toast.makeText(applicationContext, "수정되었습니다", Toast.LENGTH_SHORT)
                                toast.show()


                                finish()

                            }//로그인성공
                            "fail" -> {
                                val toast = Toast.makeText(applicationContext, "실패했습니다", Toast.LENGTH_SHORT)
                                toast.show()
                            }//로그인실패
                            "internal" -> {
                                val toast = Toast.makeText(applicationContext, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<updateInfoResponse>, t: Throwable) {
                        Log.e("실패", "$t")
                        val toast = Toast.makeText(applicationContext, "가입에 실패했습니다", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                })
        }
    }

}
