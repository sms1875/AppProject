package com.example.application.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.application.ServiceGenerator
import com.example.application.databinding.ActivityRegisterBinding
import com.example.application.network.response.*
import com.example.application.network.service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idConfirmService = ServiceGenerator.createService(idConfirmInterface::class.java)
        val registerService = ServiceGenerator.createService(registerInterface::class.java)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.editPassword2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val password=binding.editPassword.text.toString()
                val password2=binding.editPassword2.text.toString()
                if(password.equals(password2)) binding.txtPasswordConfirm.setText("일치합니다.")
                else binding.txtPasswordConfirm.setText("일치하지 않습니다.")
                Log.e("실패", password+password2)
            }
        })//비밀번호 중복 검사

        binding.btnIdConfirm.setOnClickListener {
            val userid=binding.editId.text.toString()
            idConfirmService.idConfirm(userid).enqueue(object : Callback<resultResponse> {
                override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                    val result = response.body()
                    when (result?.result) {
                        "success" -> {
                            val toast = Toast.makeText(applicationContext, "아이디 사용이 가능합니다", Toast.LENGTH_SHORT)
                            toast.show()
                            binding.btnIdConfirm.text = "중복 확인 완료"
                        }//로그인성공
                        "fail" -> {
                            val toast = Toast.makeText(applicationContext, "현재 사용중인 아이디입니다", Toast.LENGTH_SHORT)
                            toast.show()
                            binding.btnIdConfirm.text = "아이디 중복 확인"
                        }//로그인실패
                        "internal" -> {
                            val toast = Toast.makeText(applicationContext, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                }
                override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                    Log.e("실패", "$t")
                    val toast = Toast.makeText(applicationContext, "중복 체크에 실패했습니다", Toast.LENGTH_SHORT)
                    toast.show()
                }
            })
        }//아이디 중복검사 버튼

        binding.btnRegister.setOnClickListener {
            val userid=binding.editId.text.toString()
            val password=binding.editPassword.text.toString()
            val password2=binding.editPassword2.text.toString()
            val name=binding.editName.text.toString()
            val idConfirm = binding.btnIdConfirm.text.toString()
            val usertel = binding.editPhoneNum.text.toString()


            if(userid == ""|| password== ""|| name== "") {
                val toast = Toast.makeText(applicationContext, "빈칸 없이 입력하세요", Toast.LENGTH_SHORT)
                toast.show()
            }//빈칸이 있는 경우
            else if(password!=password2){
                val toast = Toast.makeText(applicationContext, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT)
                toast.show()
            }//비밀번호 확인이 다를 경우
            else if(idConfirm!="중복 확인 완료"){
                val toast = Toast.makeText(applicationContext, "아이디 중복을 확인해주세요", Toast.LENGTH_SHORT)
                toast.show()
            }//아이디 중복체크를 안한경우
            else{
                registerService.register(userid, password,usertel,name).enqueue(object : Callback<resultResponse> {
                    override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                        val result = response.body()
                        when (result?.result) {
                            "success" -> {
                                Log.d("성공", "$result")
                                val toast = Toast.makeText(applicationContext, "회원가입 완료", Toast.LENGTH_SHORT)
                                toast.show()
                                finish()
                            }//로그인성공
                            "fail" -> {
                                val toast = Toast.makeText(applicationContext, "회원가입에 실패했습니다", Toast.LENGTH_SHORT)
                                toast.show()
                            }//로그인실패
                            "internal" -> {
                                val toast = Toast.makeText(applicationContext, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                        Log.e("실패", "$t")
                        val toast = Toast.makeText(applicationContext, "가입에 실패했습니다", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                })
            }
        }//회원가입 버튼
    }
}
