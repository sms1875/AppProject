package com.example.application.ui.Home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.application.*
import com.example.application.ui.Class.ClassInfo.ClassInfoActivity
import com.example.application.databinding.FragmentClassSignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import kotlin.collections.ArrayList
import com.example.application.network.response.*
import com.example.application.network.service.*

class SignupClassFragment : Fragment() {


    private var _binding: FragmentClassSignupBinding? = null
    private val binding get() = _binding!!

    private var completedClassList = mutableListOf<classData>()


    lateinit var completedClassAdapter: OpenClassAdapter

    var TOKEN=SharedPreferences.prefs.getString("key","key")


    val getCompletedClassService = ServiceGenerator.createService(getCompletedClassInterface::class.java,TOKEN)
    val reviewClassService = ServiceGenerator.createService(reviewClassInterface::class.java,TOKEN)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassSignupBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getCompletedClassService.getCompletedClass().enqueue(object : Callback<classDataList> {
            override fun onResponse(
                call: Call<classDataList>,
                response: Response<classDataList>
            ) {
                if (response.body()?.classData != null) {
                    completedClassList = response.body()?.classData as ArrayList<classData>
                    Log.d("a", response.body().toString())
                    initRecycler()
                }
            }

            override fun onFailure(call: Call<classDataList>, t: Throwable) {
                Log.d("b:", "실패 : $t")
            }
        })


    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecycler() {

        completedClassAdapter = OpenClassAdapter(requireContext())
        binding.rvSignupClass.adapter = completedClassAdapter
        completedClassAdapter.buttonText="리뷰 등록"
        binding.rvSignupClass.addItemDecoration(VerticalItemDecorator(20))
        binding.rvSignupClass.addItemDecoration(HorizontalItemDecorator(10))

        completedClassAdapter.datas = completedClassList
        completedClassAdapter.setOnItemClickListener(object : OpenClassAdapter.OnItemClickListener {
            override fun onButtonClick(v: View, data: classData, pos: Int) {
                var reviewContent=""
                var reviewScore ="5"

                AlertDialog.Builder(context)
                    .setView(R.layout.fragment_review_dialog)
                    .setNegativeButton("취소", null)
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->


                        reviewClassService.reviewClass(data.classId,reviewContent,reviewScore).enqueue(object : Callback<resultResponse> {
                            override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                                Log.d("결과:", response.toString())
                            }
                            override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                                Log.d("결과:", "실패 : $t")
                            }
                        })})
                    .show()
                    .also { alertDialog ->

                        if (alertDialog == null) {
                            return@also
                        }
                        var edit1: EditText? =  alertDialog.findViewById(R.id.edit_classReview)
                        var edit2: RatingBar? =  alertDialog.findViewById(R.id.userReviewRatingBar)

                        edit1?.doOnTextChanged { text, start, before, count ->
                            reviewContent= edit1.text.toString()
                        }

                        edit2?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            reviewScore= rating.toString()
                            Log.d("bbbbbb", reviewScore)
                        }
                    }

            }

            override fun onItemClick(v: View, data: classData, pos : Int) {
                Intent(context, ClassInfoActivity::class.java).apply {
                    this.putExtra("classID",data.classId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })
        completedClassAdapter.notifyDataSetChanged()
    }
}




