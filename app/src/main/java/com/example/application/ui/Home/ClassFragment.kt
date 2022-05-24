package com.example.application.ui.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.application.*
import com.example.application.ui.Class.ClassInfo.ClassInfoActivity
import com.example.application.databinding.FragmentClassBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.application.network.response.*
import com.example.application.network.service.*

import com.example.application.network.response.*
class ClassFragment : Fragment() {


    private var _binding: FragmentClassBinding? = null
    private val binding get() = _binding!!

    lateinit var myClassListAdapter: ClassAdapter
    private var myClassList = mutableListOf<classData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TOKEN=SharedPreferences.prefs.getString("key","key")
        val getClassService = ServiceGenerator.createService(MyClassInterface::class.java,TOKEN)

        getClassService.getMyClassList().enqueue(object : Callback<classDataList> {
            override fun onResponse(call: Call<classDataList>, response: Response<classDataList>) {
                if(response.body()?.classData !=null) {
                    myClassList = response.body()?.classData as ArrayList<classData>
                    initRecycler()
                }
                else
                    binding.textclass.text = "수강중인 강의가 없습니다"

            }
            override fun onFailure(call: Call<classDataList>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
            }
        })

        var classData1 = classData("test", "tutor", "5000", "", "id", "1")
        var classData2 = classData("name", "tutor", "4000", "", "i2", "2")

        var classD = listOf(classData1, classData2)
        //myClassList = classD as MutableList<classData>


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        myClassListAdapter = ClassAdapter(requireContext())
        binding.rvMyClass.layoutManager= GridLayoutManager(context, 2)
        binding.rvMyClass.adapter = myClassListAdapter
        binding.rvMyClass.addItemDecoration(VerticalItemDecorator(5))
        binding.rvMyClass.addItemDecoration(HorizontalItemDecorator(10))

        myClassListAdapter.datas = myClassList
        myClassListAdapter.notifyDataSetChanged()
        myClassListAdapter.setOnItemClickListener(object : ClassAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: classData, pos: Int) {
                Intent(context, ClassInfoActivity::class.java).apply {
                    this.putExtra("classID", data.classId)
                    this.putExtra("myClass", "myClass")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })
    }

}




