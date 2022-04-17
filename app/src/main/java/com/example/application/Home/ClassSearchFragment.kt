package com.example.application.Class

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.application.*
import com.example.application.Class.ClassInfo.ClassInfoActivity
import com.example.application.databinding.FragmentClassSearchBinding
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.Toast

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


class ClassSearchFragment : Fragment() {

    private var _binding: FragmentClassSearchBinding? = null
    private val binding get() = _binding!!
    lateinit var searchedClassListAdapter: ClassAdapter

    private var searchedClassList = mutableListOf<classData>()

    private var category = ""
    private var string = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val classSearchService = ServiceGenerator.createService(classSearchInterface::class.java)

        binding.editClassSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                category=""
                string=binding.editClassSearch.text.toString()
/*
                if(binding.btnCategory1.isChecked==true) category+="A%"
                if(binding.btnCategory2.isChecked==true) category+="B%"
                if(binding.btnCategory3.isChecked==true) category+="C%"
                if(binding.btnCategory4.isChecked==true) category+="D%"
*/

                classSearchService.classSearch(string,category).enqueue(object : Callback<classDataList> {
                    override fun onResponse(call: Call<classDataList>, response: Response<classDataList>) {
                        Log.d("결과:", response.body().toString())
                        if (response.body()?.classData==null) {
                            searchedClassList =mutableListOf<classData>()
                        }
                        else
                            searchedClassList=response.body()?.classData as ArrayList<classData>
                        initRecycler()
                    }
                    override fun onFailure(call: Call<classDataList>, t: Throwable) {
                        Log.d("결과:", "실패 : $t")
                    }
                })


            }
            false
        }

        binding.btnCategory1.setOnClickListener {
            Toast.makeText(requireContext(), "구현중입니다", Toast.LENGTH_SHORT).show()
        }
        binding.btnCategory2.setOnClickListener {
            Toast.makeText(requireContext(), "구현중입니다", Toast.LENGTH_SHORT).show()
        }
        binding.btnCategory3.setOnClickListener {
            Toast.makeText(requireContext(), "구현중입니다", Toast.LENGTH_SHORT).show()
        }
        binding.btnCategory4.setOnClickListener {
            Toast.makeText(requireContext(), "구현중입니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecycler() {
        searchedClassListAdapter = ClassAdapter(requireContext())
        binding.rvSearchedClass.layoutManager= GridLayoutManager(context, 2)
        binding.rvSearchedClass.adapter = searchedClassListAdapter
        binding.rvSearchedClass.addItemDecoration(VerticalItemDecorator(5))
        binding.rvSearchedClass.addItemDecoration(HorizontalItemDecorator(10))

        searchedClassListAdapter.datas = searchedClassList
        searchedClassListAdapter.notifyDataSetChanged()
        searchedClassListAdapter.setOnItemClickListener(object : ClassAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: classData, pos: Int) {
                Intent(context, ClassInfoActivity::class.java).apply {
                    this.putExtra("classID", data.classId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })
    }
}

var classSearchFragment = ClassSearchFragment()


interface classSearchInterface {
    @FormUrlEncoded
    @POST("/db/class/search")
    fun classSearch(
        @Field("string") string:String,
        @Field("category") category:String,
    ): Call<classDataList>
}//강의 취소