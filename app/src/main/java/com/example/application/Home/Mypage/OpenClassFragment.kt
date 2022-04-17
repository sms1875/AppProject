package com.example.application.Home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.application.*
import com.example.application.Class.ClassInfo.ClassInfoActivity
import com.example.application.databinding.FragmentOpenClassBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList


class OpenClassFragment : Fragment() {


    private var _binding: FragmentOpenClassBinding? = null
    private val binding get() = _binding!!

    private var openClassList = mutableListOf<classData>()
    private var completedClassList = mutableListOf<classData>()

    lateinit var OpenclassAdapter: OpenClassAdapter
    lateinit var completedClassAdapter: OpenClassAdapter

    var TOKEN=SharedPreferences.prefs.getString("key","key")

    val getClassService = ServiceGenerator.createService(getMyOpenedClassInterface::class.java,TOKEN)
    val getMyOpenedCompletedClassService = ServiceGenerator.createService(getMyOpenedCompletedClassInterface::class.java,TOKEN)
    val cancelClassRegisterService = ServiceGenerator.createService(cancelClassRegisterInterface::class.java,TOKEN)
    val reopenClassService = ServiceGenerator.createService(reopenClassInterface::class.java,TOKEN)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOpenClassBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getClassService.getMyOpenedClass().enqueue(object : Callback<classDataList> {
            override fun onResponse(
                call: Call<classDataList>,
                response: Response<classDataList>
            ) {
                if (response.body()?.classData != null) {
                    openClassList = response.body()?.classData as ArrayList<classData>
                    Log.d("a", response.body().toString())
                    initRecycler()
                }
            }

            override fun onFailure(call: Call<classDataList>, t: Throwable) {
                Log.d("a:", "실패 : $t")
            }
        })

        getMyOpenedCompletedClassService.getMyOpenedCompletedClass().enqueue(object : Callback<classDataList> {
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
        OpenclassAdapter = OpenClassAdapter(requireContext())
        binding.rvOpenClass.adapter = OpenclassAdapter
        OpenclassAdapter.buttonText="강의 취소"
        binding.rvOpenClass.addItemDecoration(VerticalItemDecorator(20))
        binding.rvOpenClass.addItemDecoration(HorizontalItemDecorator(10))

        OpenclassAdapter.datas = openClassList
        OpenclassAdapter.setOnItemClickListener(object : OpenClassAdapter.OnItemClickListener {
            override fun onButtonClick(v: View, data: classData, pos: Int) {

                cancelClassRegisterService.cancelClassRegister(data.classId).enqueue(object : Callback<resultResponse> {
                    override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                        Log.d("결과:", response.toString())
                    }
                    override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                        Log.d("결과:", "실패 : $t")
                    }
                })
            }

            override fun onItemClick(v: View, data: classData, pos : Int) {
                Intent(context, ClassInfoActivity::class.java).apply {
                    this.putExtra("classID",data.classId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })
        OpenclassAdapter.notifyDataSetChanged()

        completedClassAdapter = OpenClassAdapter(requireContext())
        binding.rvCompletedClass.adapter = completedClassAdapter
        completedClassAdapter.buttonText="강의 재오픈"
        binding.rvCompletedClass.addItemDecoration(VerticalItemDecorator(20))
        binding.rvCompletedClass.addItemDecoration(HorizontalItemDecorator(10))

        completedClassAdapter.datas = completedClassList
        completedClassAdapter.setOnItemClickListener(object : OpenClassAdapter.OnItemClickListener {
            override fun onButtonClick(v: View, data: classData, pos: Int) {
                var start_date=""
                var start_time =""
                var end_date =""
                var end_time =""

                AlertDialog.Builder(context)
                    .setView(R.layout.fragment_reopen_dialog)
                    .setNegativeButton("취소", null)
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        reopenClassService.reopenClass(data.classId,start_date,start_time,end_date,end_time ).enqueue(object : Callback<resultResponse> {
                            override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                                Log.d("결과:", response.toString())
                            }
                            override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                                Log.d("결과:", "실패 : $t")
                            }
                        })
                    })
                    .show()
                    .also { alertDialog ->

                        if(alertDialog == null) {
                            return@also
                        }
                        var edit1: Button? = alertDialog.findViewById(R.id.reopenStartDate)
                        var edit2: Button? = alertDialog.findViewById(R.id.reopenEndDate)
                        var edit3: Button? = alertDialog.findViewById(R.id.reopenStartTime)
                        var edit4: Button? = alertDialog.findViewById(R.id.reopenEndime)

                        edit1?.setOnClickListener {
                            Log.d("a","tetawetaw")
                            val cal = Calendar.getInstance()
                            val dateSetListener =
                                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                    start_date = "${year}-${month + 1}-${dayOfMonth}"
                                    edit1.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                                }
                            DatePickerDialog(
                                context!!,
                                dateSetListener,
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }

                        edit2?.setOnClickListener {
                            val cal = Calendar.getInstance()
                            val dateSetListener =
                                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                    end_date = "${year}-${month + 1}-${dayOfMonth}"
                                    edit2.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                                }
                            DatePickerDialog(
                                context!!,
                                dateSetListener,
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }

                        edit3?.setOnClickListener {
                            val cal = Calendar.getInstance()
                            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                start_time = "${hourOfDay}:${minute}"
                                edit3.text = "${hourOfDay}시 ${minute}분"
                            }
                            TimePickerDialog(
                                context!!,
                                timeSetListener,
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true
                            ).show()
                        }

                        edit4?.setOnClickListener {
                            val cal = Calendar.getInstance()
                            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                end_time = "${hourOfDay}:${minute}"
                                edit4.text = "${hourOfDay}시 ${minute}분"
                            }
                            TimePickerDialog(
                                context!!,
                                timeSetListener,
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true
                            ).show()


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


interface getMyOpenedClassInterface {
    @POST("/db/class/getMyOpenedClass")
    fun getMyOpenedClass(): Call<classDataList>
}//등록한 강의 목록

interface getMyOpenedCompletedClassInterface {
    @POST("/db/class/getMyOpenedCompletedClass")
    fun getMyOpenedCompletedClass(): Call<classDataList>
}//completed강의 목록

interface cancelClassRegisterInterface {
    @FormUrlEncoded
    @POST("/db/class/cancelRegister")
    fun cancelClassRegister(@Field("classId") classId:String,
    ): Call<resultResponse>
}//강의 취소

interface reopenClassInterface {
    @FormUrlEncoded
    @POST("/db/class/reopen")
    fun reopenClass(
        @Field("classId") classId:String,
        @Field("start_date") start_date:String,
        @Field("start_time") start_time:String,
        @Field("end_date") end_date:String,
        @Field("end_time") end_time:String
    ): Call<resultResponse>
}


