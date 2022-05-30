package com.example.application.Class

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.application.*
import com.example.application.ui.Class.ClassInfo.ClassInfoActivity
import com.example.application.databinding.FragmentClassSearchBinding
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.example.application.network.response.*
import com.example.application.network.service.*

import com.example.application.network.response.*

class ClassSearchFragment : Fragment() {

    private var _binding: FragmentClassSearchBinding? = null
    private val binding get() = _binding!!
    lateinit var searchedClassListAdapter: ClassAdapter

    private var searchedClassList = mutableListOf<classData>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val classSearchService = ServiceGenerator.createService(classSearchInterface::class.java)


        var searchString=""//검색할 이름
        var searchCategory=""//카테고리 선택
        var orderByString=""//정렬 선택
        var buttonCount=arrayOf(0,0,0)//버튼 눌린 카운트
        val buttonText=arrayOf("리뷰 점수","리뷰 개수","수강 신청 인원")//기준 text

        with(binding.spinnerClassSearchOption) {
            val typeList=resources.getStringArray(com.example.application.R.array.classCategory)
            val searchOptionAdapter = SearchOptionAdapter(context, android.R.layout.simple_spinner_dropdown_item, typeList)
            adapter = searchOptionAdapter
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(position){
                        0-> searchCategory=""
                        1-> searchCategory="A"
                        2-> searchCategory="B"
                        3-> searchCategory="C"
                        4-> searchCategory="D"
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }//검색 옵션 설정
        }//검색 카테고리 설정

        with(binding){
            btnClassSearchSort1.setOnClickListener {
                    if(buttonCount[0]==2)
                        buttonCount[0]=0
                    else
                        buttonCount[0]++
                    //버튼 클릭시 0 1 2 카운트
                    when (buttonCount[0]) {
                        0 -> btnClassSearchSort1.setText(buttonText[0])
                        1 -> btnClassSearchSort1.setText(buttonText[0]+"▲")
                        2 -> btnClassSearchSort1.setText(buttonText[0]+"▼")
                    }
                }
            btnClassSearchSort2.setOnClickListener {
                if(buttonCount[1]==2)
                    buttonCount[1]=0
                else
                    buttonCount[1]++
                //버튼 클릭시 0 1 2 카운트
                when (buttonCount[1]) {
                    0 -> btnClassSearchSort2.setText(buttonText[1])
                    1 -> btnClassSearchSort2.setText(buttonText[1]+"▲")
                    2 -> btnClassSearchSort2.setText(buttonText[1]+"▼")
                }
            }
            btnClassSearchSort3.setOnClickListener {
                if(buttonCount[2]==2)
                    buttonCount[2]=0
                else
                    buttonCount[2]++
                //버튼 클릭시 0 1 2 카운트
                when (buttonCount[2]) {
                    0 -> btnClassSearchSort3.setText(buttonText[2])
                    1 -> btnClassSearchSort3.setText(buttonText[2]+"▲")
                    2 -> btnClassSearchSort3.setText(buttonText[2]+"▼")
                }
            }
        }//버튼 클릭시 설정


        binding.btnClassSearch.setOnClickListener {

            searchString= binding.editClassSearch.text.toString()
            orderByString=""//초기화

            for(i in 0..2){
                when (buttonCount[i]) {
                    0 -> {}
                    1 -> orderByString+=i.toString()+"=+,"
                    2 -> orderByString+=i.toString()+"=-,"
                }//count 문자열 설정
            }//정렬 조건 개수

            if(orderByString!=""){
                orderByString = orderByString.substring(0, orderByString.length -1);
            }//마지막 , 제거

            classSearchService.classSearch(searchString,searchCategory,orderByString).enqueue(object : Callback<classDataList> {
                override fun onResponse(call: Call<classDataList>, response: Response<classDataList>) {
                    Log.d("결과:", response.body().toString())

                    searchedClassList=response.body()?.classData as MutableList<classData>

                    initRecycler()
                }
                override fun onFailure(call: Call<classDataList>, t: Throwable) {
                    Log.d("결과:", "실패 : $t")
                }
            })

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

