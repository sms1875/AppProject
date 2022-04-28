package com.example.application.Login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.application.*
import com.example.application.databinding.ActivityLoginBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setRationaleMessage("앱 사용을 위해서 권한 설정이 필요합니다")
            .setDeniedMessage("앱 사용을 위해서 권한 설정이 필요합니다 \n [설정] > [권한] 에서 권한을 허용할 수 있어요.")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET)
            .check();


        SharedPreferences.prefs.setString("key","")
        SharedPreferences.prefs.setString("userName","")
        SharedPreferences.prefs.setString("userType","")
        //유저 설정 초기화

        val intent_main= Intent(this,MainActivity::class.java)
        val intent_register= Intent(this,RegisterActivity::class.java)

        val signService = ServiceGenerator.createService(singInterface::class.java)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener{

            //startActivity(intent_main)
           //finish()
            startActivity(intent_register)
        }//회원가입 버튼

        binding.btnLogin.setOnClickListener {

            val userid=binding.editId.text.toString()
            val password=binding.editPassword.text.toString()
            signService.login(userid, password).enqueue(object :Callback<loginResponse> {
                override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                    val result = response.body()
                    when (result?.result) {
                        "success" -> {
                            SharedPreferences.prefs.setString("key",result.token)
                            SharedPreferences.prefs.setString("userName",result.userName)
                            SharedPreferences.prefs.setString("userType",result.userType)
                            val toast = Toast.makeText(applicationContext, "환영합니다", Toast.LENGTH_SHORT)
                            toast.show()

                            startActivity(intent_main)
                            finish()
                        }//로그인성공
                        "fail" -> {
                            val toast = Toast.makeText(applicationContext, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT)
                            toast.show()
                        }//로그인실패
                        "internal" -> {
                            val toast = Toast.makeText(applicationContext, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT)
                            toast.show()
                        }//서버오류
                    }
                }
                override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                    Log.d("fail", "$t")
                }
            })
        } //로그인 버튼
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(this@LoginActivity, "", Toast.LENGTH_SHORT).show()
        }
        override fun onPermissionDenied(deniedPermissions: ArrayList<String?>) {
            finish();
        }
    }

}

interface singInterface {
    @FormUrlEncoded
    @POST("/db/login")
    fun login(@Field("userid") userid:String,
              @Field("password") pw:String
    ) : Call<loginResponse>
}//로그인 인터페이스

