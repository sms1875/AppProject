package com.example.application.ui.Home.Menu.HopeClass

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.databinding.FragmentHopeClassForumBinding
import com.example.application.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.example.application.network.service.*
import com.example.application.network.response.*

class HopeClassForumFragment : Fragment() {

    private var _binding: FragmentHopeClassForumBinding? = null
    private val binding get() = _binding!!


    lateinit var hopeClassPage:hopeClassPage
    private var hopeClasstopicList = mutableListOf<hopeClassTopic>()
    lateinit var hopeClassForumAdapter:HopeClassForumAdapter

    companion object {
        fun newInstant(myHopeClass: String) : HopeClassForumFragment {
            val fragment = HopeClassForumFragment()
            val args = Bundle()
            args.putString("myHopeClass", myHopeClass)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHopeClassForumBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val TOKEN = SharedPreferences.prefs.getString("key","key")
        val getHopeClassTopicService = ServiceGenerator.createService(hopeClassForumInterface::class.java)
        val getmyHopedClassService = ServiceGenerator.createService(myHopedClassInterface::class.java,TOKEN)
        val getSearchedHopeClassTopicService = ServiceGenerator.createService(hopeClassSearchInterface::class.java)

        if(arguments?.getString("myHopeClass")=="myHopeClass"){
            binding.hopeClassForumLayout.visibility=View.GONE
            binding.hopeClassForumLayout.isEnabled=false
            binding.txtClassName.text="나의 희망강의 목록"
            getmyHopedClassService.getmyHopedClassList().enqueue(object : Callback<List<hopeClassTopic>> {
                override fun onResponse(call: Call<List<hopeClassTopic>>, response: Response<List<hopeClassTopic>>) {
                    hopeClasstopicList= response.body() as MutableList<hopeClassTopic>

                    setForum(TOKEN)
                }
                override fun onFailure(call: Call<List<hopeClassTopic>>, t: Throwable) {
                    Log.d("결과:", "실패 : $t")
                }
            })
        }
        else{
            getHopeClassTopicService.getHopeClassTopicList().enqueue(object : Callback<List<hopeClassTopic>> {
                override fun onResponse(call: Call<List<hopeClassTopic>>, response: Response<List<hopeClassTopic>>) {
                    hopeClasstopicList= response.body() as MutableList<hopeClassTopic>

                    setForum(TOKEN)
                }
                override fun onFailure(call: Call<List<hopeClassTopic>>, t: Throwable) {
                    Log.d("결과:", "실패 : $t")
                }
            })//희망 목록 받아오기
        }
        var searchCategory=""//검색옵션
        with(binding.spinnerHopeClassSearchOption) {
            val typeList=resources.getStringArray(R.array.classCategory)
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
        }

        binding.btnHopeClassPost.setOnClickListener {
            (activity as HopeClassActivtiy).hopeClassPostFragment()
        }

        binding.btnHopeClassSearch.setOnClickListener {

            var searchString=""
            searchString= binding.editHopeClassSearch.text.toString()

            getSearchedHopeClassTopicService.getSearchedHopeClass(searchString,searchCategory).enqueue(object : Callback<List<hopeClassTopic>> {
                override fun onResponse(call: Call<List<hopeClassTopic>>, response: Response<List<hopeClassTopic>>) {
                    Log.d("1121",response.toString())
                    Log.d("1121",response.body().toString())
                    hopeClasstopicList= response.body() as MutableList<hopeClassTopic>

                    setForum(TOKEN)
                }
                override fun onFailure(call: Call<List<hopeClassTopic>>, t: Throwable) {
                    Log.d("결과:", "실패 : $t")
                }
            })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun setForum(TOKEN:String){
        with(binding.rvHopeClassTopicList){
            hopeClassForumAdapter = HopeClassForumAdapter(hopeClasstopicList)
            adapter=hopeClassForumAdapter
            layoutManager= LinearLayoutManager(requireContext())
            hopeClassForumAdapter.setOnItemclickListner(object: HopeClassForumAdapter.OnItemClickListner{
                override fun onItemClick(view: View, position: Int, data: hopeClassTopic) {

                    val hopedClassDetailService = ServiceGenerator.createService(hopedClassDetailInterface::class.java,TOKEN)
                    hopedClassDetailService.getHopedClassDetail(data.topicId.toInt()).enqueue(object :
                        Callback<hopeClassPage> {
                        override fun onResponse(call: Call<hopeClassPage>, response: Response<hopeClassPage>) {
                            hopeClassPage= response.body()!!
                            (activity as HopeClassActivtiy).hopeClasspageFragment(hopeClassPage,data.topicId)
                        }
                        override fun onFailure(call: Call<hopeClassPage>, t: Throwable) {
                            Log.d("결과:", "실패 : $t")
                        }
                    })//희망 목록 받아오기
                }
            })
        }
    }
}

