package com.example.application.ui.Home

import com.example.application.network.service.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.application.*
import com.example.application.ui.Home.Menu.Place.PlaceDetailActivity
import com.example.application.databinding.FragmentMyRegisterPlaceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.POST

import com.example.application.network.response.*
class MyRegisterPlaceFragment : Fragment() {

    private var binding : FragmentMyRegisterPlaceBinding? = null

    lateinit var placeAdapter: PlaceAdapter
    private var placeList = mutableListOf<placeData>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyRegisterPlaceBinding.inflate(inflater, container, false)

        var TOKEN= SharedPreferences.prefs.getString("key","key")
        val getMyRegisterPlaceService = ServiceGenerator.createService(getMyRegisterPlaceInterface::class.java,TOKEN)

        getMyRegisterPlaceService.getMyRegisterPlaceList().enqueue(object :
            Callback<placeDataList> {
            override fun onResponse(call: Call<placeDataList>, response: Response<placeDataList>) {
                if(response.body()?.space !=null) {
                    placeList = response.body()?.space as ArrayList<placeData>
                    initRecycler()
                    Log.d("33333", "성공 : ${response.body()}")

                }
            }
            override fun onFailure(call: Call<placeDataList>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
            }
        })

        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
    private fun initRecycler() {
        placeAdapter = PlaceAdapter(requireContext())
        binding?.rvProfile?.adapter = placeAdapter
        binding?.rvProfile?.addItemDecoration(VerticalItemDecorator2(20))
        binding?.rvProfile?.addItemDecoration(HorizontalItemDecorator2(10))
        placeAdapter.datas = placeList
        placeAdapter.notifyDataSetChanged()
        placeAdapter.setOnItemClickListener(object : PlaceAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: placeData, pos: Int) {

                Intent(context, PlaceDetailActivity::class.java).apply {
                    this.putExtra("placeInfo",data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })
    }
}


