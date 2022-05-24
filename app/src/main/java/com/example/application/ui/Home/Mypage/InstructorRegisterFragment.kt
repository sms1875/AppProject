package com.example.application.ui.Home.Mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.application.*
import com.example.application.databinding.FragmentInstructorRegistrerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

import com.example.application.network.response.*
import com.example.application.network.service.*
class InstructorRegisterFragment : Fragment() {

    private var _binding : FragmentInstructorRegistrerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInstructorRegistrerBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TOKEN = SharedPreferences.prefs.getString("key", "token")
        val instructorRegisterService = ServiceGenerator.createService(instructorRegisterInterface::class.java, TOKEN)


        binding.btnInstructorRegister.setOnClickListener {
            instructorRegisterService.register(
                binding.editCertificate.text.toString(),
                binding.editInstructorHistory.text.toString(),
                binding.editInstructorField.text.toString(),
                binding.editInstructorInfo.text.toString()
            ).enqueue(object : Callback<resultResponse> {
                override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                    val toast = Toast.makeText(requireContext(), "신청되었습니다", Toast.LENGTH_SHORT)
                    toast.show()
                    val fragmentManager: FragmentManager = activity!!.supportFragmentManager
                    fragmentManager.beginTransaction().remove(this@InstructorRegisterFragment)
                        .commit()
                    fragmentManager.popBackStack()
                }
                override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                    Log.d("b:", "실패 : $t")
                }
            })
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}
