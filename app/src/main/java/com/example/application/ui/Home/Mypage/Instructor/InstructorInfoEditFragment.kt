package com.example.application.ui.Home.Mypage.Instructor
import com.example.application.network.service.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.application.*
import com.example.application.databinding.FragmentInstructorInfoEditBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import com.example.application.network.response.*
class InstructorInfoEditFragment : Fragment() {

    private var _binding : FragmentInstructorInfoEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInstructorInfoEditBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TOKEN = SharedPreferences.prefs.getString("key", "token")
        val instructorInfoEditService = ServiceGenerator.createService(instructorInfoEditInterface::class.java, TOKEN)
        val getInstructorInfoService = ServiceGenerator.createService(getInstructorInfoInterface::class.java, TOKEN)

        getInstructorInfoService.getInstructorInfo().enqueue(object : Callback<instructorInfo> {
            override fun onResponse(call: Call<instructorInfo>, response: Response<instructorInfo>) {
                val info : instructorInfo = response.body()!!
                with(binding){
                    editCertificate.hint=info.licenseInfo
                    editInstructorField.hint=info.areaInfo
                    editInstructorHistory.hint=info.historyInfo
                    editInstructorInfo.hint=info.selfIntroduce
                }
            }
            override fun onFailure(call: Call<instructorInfo>, t: Throwable) {
                Log.d("b:", "실패 : $t")
            }
        })

        binding.btnInstructorInfoEdit.setOnClickListener {
            instructorInfoEditService.editInstructorInfo(
                binding.editCertificate.text.toString(),
                binding.editInstructorHistory.text.toString(),
                binding.editInstructorField.text.toString(),
                binding.editInstructorInfo.text.toString()
            ).enqueue(object : Callback<resultResponse> {
                override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                    val toast = Toast.makeText(requireContext(), "수정되었습니다", Toast.LENGTH_SHORT)
                    toast.show()
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

