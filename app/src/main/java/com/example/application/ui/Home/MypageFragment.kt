package com.example.application.ui.Home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.application.ui.Class.ClassCreateActivity
import com.example.application.ui.user.LoginActivity
import com.example.application.databinding.FragmentMypageBinding
import com.example.application.*
import com.example.application.ui.Home.Mypage.Instructor.OpenClassFragment
import com.example.application.ui.user.UserInfoActivity
import com.example.application.ui.Home.Mypage.User.InstructorRegisterFragment
import com.example.application.ui.Home.Mypage.User.ScheduleActivity
import com.example.application.ui.Home.Mypage.User.SignupClassFragment
import com.example.application.ui.MainActivity


class MypageFragment : Fragment() {

    private var _binding : FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSchedule.visibility=View.VISIBLE
        binding.btnSchedule.isEnabled=true

        if(SharedPreferences.prefs.getString("userName","")!=""){
            binding.txtName.text=SharedPreferences.prefs.getString("userName","")+"님 환영합니다"

            if(SharedPreferences.prefs.getString("userType","")=="강사"){
                binding.tutorLayout.visibility=View.VISIBLE
                binding.tutorLayout.isEnabled=true
            }
        }//로그인 후 설정


        binding.btnMyPage.setOnClickListener {
            val intent_userInfo= Intent(context, UserInfoActivity::class.java)
            startActivity(intent_userInfo)
        }

        binding.btnSchedule.setOnClickListener {
            val intent_schedule= Intent(context, ScheduleActivity::class.java)
            startActivity(intent_schedule)
        }

        binding.btnLogout.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("미관중개소")
            dialog.setMessage("정말 로그아웃을 하시겠습니까?")
            dialog.setIcon(R.drawable.ic_launcher_foreground)
            fun toast_p() {
                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

                SharedPreferences.prefs.setString("key","")
                SharedPreferences.prefs.setString("userName","")
                SharedPreferences.prefs.setString("userType","")

                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)


                activity?.finish()//메인액티비티 종료
            }

            val dialog_listener = object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which){
                        DialogInterface.BUTTON_POSITIVE ->
                            toast_p()
                    }
                }
            }
            dialog.setPositiveButton("YES",dialog_listener)
            dialog.setNegativeButton("NO",null)
            dialog.show()
        }


        if(SharedPreferences.prefs.getString("userType","")=="강사"){
            binding.tutorLayout.visibility=View.VISIBLE
            binding.tutorLayout.isEnabled=true
        }

        binding.btnEditTutorInfo.setOnClickListener {
            Toast.makeText(requireContext(), "구현중입니다", Toast.LENGTH_SHORT).show()
        }

        binding.btnAppSetting.setOnClickListener {
            Toast.makeText(requireContext(), "구현중입니다", Toast.LENGTH_SHORT).show()
        }

        binding.btnClassCreate.setOnClickListener {
            val intent_classCreate= Intent(context, ClassCreateActivity::class.java)
            startActivity(intent_classCreate)
        }

        binding.btnMyClassList.setOnClickListener {
            (activity as MainActivity).openClassFragment()
        }

        binding.btnClassHistory.setOnClickListener {
            (activity as MainActivity).SignupClassFragment()
        }

        binding.btnInstructorRegister.setOnClickListener {
            (activity as MainActivity).InstructorRegisterFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}

var openClassFragment = OpenClassFragment()
var singupClassFragment = SignupClassFragment()
var instructorRegisterFragment = InstructorRegisterFragment()
var reservationPlaceFragment = ReservationPlaceFragment()
var myRegisterPlaceFragment = MyRegisterPlaceFragment()