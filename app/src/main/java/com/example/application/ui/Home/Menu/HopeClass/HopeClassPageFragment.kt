package com.example.application.ui.Home.Menu.HopeClass


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.application.*
import com.example.application.ui.Class.ClassCreateActivity
import com.example.application.databinding.FragmentHopeClassPageBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

import com.example.application.network.response.*
import com.example.application.network.service.*
class HopeClassPageFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHopeClassPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<MyItem>

    var lat : Double? =37.52487
    var lng : Double? = 126.92723

    lateinit var hopeClassPage:hopeClassPage
    lateinit var topicId:String

    companion object {
        fun newInstant(hopeClassPage: hopeClassPage,topicId: String) : HopeClassPageFragment {
            val fragment = HopeClassPageFragment()
            val args = Bundle()
            args.putParcelable("hopeClassPage", hopeClassPage)
            args.putString("topicId", topicId)

            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHopeClassPageBinding.inflate(inflater, container, false)
        val view = binding.root

        hopeClassPage = arguments?.getParcelable<hopeClassPage>("hopeClassPage")!!
        topicId = arguments?.getString("topicId")!!

        Log.d("test",hopeClassPage.toString())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TOKEN = SharedPreferences.prefs.getString("key","key")
        val hopeClassTopicLikeService = ServiceGenerator.createService(hopeClassTopicLikeInterface::class.java,TOKEN)
        val hopeClassTopicLikeCancelService = ServiceGenerator.createService(hopeClassTopicLikeCancelInterface::class.java,TOKEN)

        val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(SharedPreferences.prefs.getString("userType","")=="??????"){
            with(binding.btnCreatHopeClass){
                visibility=View.VISIBLE
                isEnabled=true
                setOnClickListener {
                    val intent_classCreate= Intent(context, ClassCreateActivity::class.java)
                    intent_classCreate.putExtra("topicId",topicId)
                    startActivity(intent_classCreate)
                }
            }
        }

        with(hopeClassPage) {
            binding.txtHopeClassPostInfoTitle.text = desiredTitle
            binding.txtHopeClassPostInfoCategory.text = category
            binding.txtClassInfo.text = classDesc

            BarChartGraph(binding.hopeClassPostInfoBarChart1,
                listOf(personInfo.underThree,personInfo.ThreeAndTen,personInfo.TenAndTwentyfive,personInfo.overTwentyfive),
                listOf("3??? ??????","4~10???","11~25???","????????????"))

            BarChartGraph(binding.hopeClassPostInfoBarChart2,
                listOf(timeInfo.Morning,timeInfo.Afternoon,timeInfo.Night,timeInfo.Overnight),
                listOf("??????","??????","??????","??????"))

            BarChartGraph(binding.hopeClassPostInfoBarChart3,
                listOf(dateInfo.Monday,dateInfo.Tuesday,dateInfo.Wednesday,dateInfo.Thursday,dateInfo.Friday,dateInfo.Saturday,dateInfo.Sunday),
                listOf("???","???","???","???","???","???","???"))

            PieChartGraph(binding.hopeClassPostInfoPieChart1,
                listOf(typeInfo.Oneday,typeInfo.Regular),
                listOf("????????? ?????????","?????? ??????"))

            PieChartGraph(binding.hopeClassPostInfoPieChart2,
                listOf(onOffInfo.online,onOffInfo.offline),
                listOf("?????????","????????????"))

            when(registered){
                0->{
                    binding.btnLike.setOnClickListener {
                        val dialog=HopeClassDialogFragment()
                        activity?.supportFragmentManager?.let { fragmentManager ->
                            dialog.show(fragmentManager, "TAG_EVENT_DIALOG")
                            dialog.setDialogResult(object : HopeClassDialogFragment.OnMyDialogResult {
                                override fun finish(person: String, day: String, hopedClassTime: String, isOn: String, isOff: String, type: String, region: String) {
                                    hopeClassTopicLikeService.hopeClassTopicLike(topicId,person, day, hopedClassTime, isOn, isOff, region, type).enqueue(object :
                                        Callback<resultResponse> {
                                        override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                                            val result = response.body()
                                            when (result?.result) {
                                                "success" -> {
                                                    val toast = Toast.makeText(requireContext(), "?????????????????????", Toast.LENGTH_SHORT)
                                                    toast.show()
                                                    remove()
                                                }//
                                                "fail" -> {
                                                    val toast = Toast.makeText(requireContext(), "????????? ??????????????????", Toast.LENGTH_SHORT)
                                                    toast.show()
                                                }//
                                                "internal" -> {
                                                    val toast = Toast.makeText(requireContext(), "????????? ?????? ??????????????????", Toast.LENGTH_SHORT)
                                                    toast.show()
                                                }//
                                            }
                                        }
                                        override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                                            Log.d("??????:", "?????? : $t")
                                        }
                                    })
                                }
                            })
                        }

                    }
                }
                1->{
                    binding.btnLike.text="?????? ??????"
                    binding.btnLike.setOnClickListener {
                        hopeClassTopicLikeCancelService.hopeClassTopicLikeCancel(topicId).enqueue(object : Callback<resultResponse> {
                            override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                                Log.d("??????:", "?????? : $response")
                                remove()
                            }
                            override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                                Log.d("??????:", "?????? : $t")
                            }
                        })//?????? ?????? ????????????

                    }

                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun remove(){
        val toast = Toast.makeText(requireContext(), "?????????????????????", Toast.LENGTH_SHORT)
        toast.show()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().remove(this).commit()
        fragmentManager.popBackStack()
    }

    //?????? ??????
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val startLegion = LatLng(36.38, 127.51)

        with(googleMap.uiSettings){
            isTiltGesturesEnabled=false
            isRotateGesturesEnabled=false
            isZoomControlsEnabled=true
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLegion))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(6.8f))
        mMap.setMinZoomPreference(6.0f)
        mMap.setMaxZoomPreference(10.5f)


        clusterManager = ClusterManager(context, googleMap)
        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)

        addItems(hopeClassPage.placeInfo)

    }

    inner class MyItem(lat: Double, lng: Double, title: String, snippet: String) : ClusterItem {
        private val position: LatLng
        private val title: String
        private val snippet: String

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String {
            return title
        }

        override fun getSnippet(): String {
            return snippet
        }

        init {
            position = LatLng(lat, lng)
            this.title = title
            this.snippet = snippet
        }
    }

    private fun addItems(data:List<hopeClassPlaceInfo>) {
        for (i in 0..data.lastIndex) {
            if(data[i].placeX!="") {
                val region= data[i].desiredPlace.split("_").toMutableList()
                when(region[0]){
                    "seoul"->{
                        region[0]="???????????????"
                        val array = resources.getStringArray(R.array.region_seoul)
                        region[1]=array[region[1].toInt()]
                    }
                    "busan"->{
                        region[0]="???????????????"
                        val array = resources.getStringArray(R.array.region_buesan)
                        region[1]=array[region[1].toInt()]
                    }
                    "daegu"->{
                        region[0]="???????????????"
                        val array = resources.getStringArray(R.array.region_daegu)
                        region[1]=array[region[1].toInt()]
                    }
                    "incheon"->{
                        region[0]="???????????????"
                        val array = resources.getStringArray(R.array.region_seoul)
                        region[1]=array[region[1].toInt()]
                    }
                    "gwangju"->{
                        region[0]="???????????????"
                        val array = resources.getStringArray(R.array.region_gwangju)
                        region[1]=array[region[1].toInt()]
                    }
                    "daejeon"->{
                        region[0]="???????????????"
                        val array = resources.getStringArray(R.array.region_daejeon)
                        region[1]=array[region[1].toInt()]
                    }
                    "ulsan"->{
                        region[0]="???????????????"
                        val array = resources.getStringArray(R.array.region_ulsan)
                        region[1]=array[region[1].toInt()]
                    }
                    "sejong"->{
                        region[0]="?????????????????????"
                        val array = resources.getStringArray(R.array.region_sejong)
                        region[1]=array[region[1].toInt()]
                    }
                    "gyeonggi"->{
                        region[0]="?????????"
                        val array = resources.getStringArray(R.array.region_gyeonggi)
                        region[1]=array[region[1].toInt()]
                    }
                    "gangwon"->{
                        region[0]="?????????"
                        val array = resources.getStringArray(R.array.region_gangwon)
                        region[1]=array[region[1].toInt()]
                    }
                    "chungbuk"->{
                        region[0]="????????????"
                        val array = resources.getStringArray(R.array.region_chungbuk)
                        region[1]=array[region[1].toInt()]
                    }
                    "chungnam"->{
                        region[0]="????????????"
                        val array = resources.getStringArray(R.array.region_chungnam)
                        region[1]=array[region[1].toInt()]
                    }
                    "jeonbuk"->{
                        region[0]="????????????"
                        val array = resources.getStringArray(R.array.region_jeonbuk)
                        region[1]=array[region[1].toInt()]
                    }
                    "jeonnam"->{
                        region[0]="????????????"
                        val array = resources.getStringArray(R.array.region_jeonnam)
                        region[1]=array[region[1].toInt()]
                    }
                    "gyeongbuk"->{
                        region[0]="????????????"
                        val array = resources.getStringArray(R.array.region_gyeongbuk)
                        region[1]=array[region[1].toInt()]
                    }
                    "gyeongnam"->{
                        region[0]="????????????"
                        val array = resources.getStringArray(R.array.region_gyeongnam)
                        region[1]=array[region[1].toInt()]
                    }
                    "jeju"->{
                        region[0]="?????????????????????"
                        val array = resources.getStringArray(R.array.region_jeju)
                        region[1]=array[region[1].toInt()]
                    }
                }//?????? ?????? ??????
                val offsetItem = MyItem(data[i].placeX.toDouble(), data[i].placeY.toDouble(),region[0]+" "+region[1] , data[i].count+"???")
                clusterManager.addItem(offsetItem)
            }
        }
    }

    private fun PieChartGraph(PieChart: PieChart, valList: List<Int>, labelList:List<String>) {
        PieChart.setTouchEnabled(false)//?????? ??????
        PieChart.setDrawEntryLabels(false)//????????????x

        val entries: ArrayList<PieEntry> = ArrayList()
        for (i in 0 until valList.size) {
            entries.add(PieEntry(valList[i].toFloat(), labelList[i]))
        }

        val dataSet = PieDataSet(entries, "")
        with(dataSet) {
            setColors(*ColorTemplate.MATERIAL_COLORS)
        }
        val valueFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val num=value.toInt()
                return   "$num"+"???"
            }
        }
        val pieData = PieData(dataSet)
        with(pieData) {
            setValueTextSize(10f)
            setValueFormatter(valueFormatter)
            setValueTextColor(Color.BLACK)
            val des = Description()
            des.text = ""
            PieChart.setDescription(des)
        }
        PieChart.data = pieData
    }

    private fun BarChartGraph(barChart:BarChart, valList: List<Int>, labelList: List<String>) {
        barChart.setTouchEnabled(false)//?????? ??????
        barChart.xAxis.isEnabled=false
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false

        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until valList.size) {
            entries.add(BarEntry((i+1).toFloat(),valList[i].toFloat(), labelList[i]))
        }

        val dataSet = BarDataSet(entries, "")
        with(dataSet) {
            setColors(*ColorTemplate.MATERIAL_COLORS)
        }
        val valueFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val num=value.toInt()
                return   "$num"+"???"
            }
        }
        val barData = BarData(dataSet)
        with(barData) {
            setValueTextSize(10f)
            setValueFormatter(valueFormatter)
            setValueTextColor(Color.BLACK)
            val des = Description()
            des.text = ""
            barChart.setDescription(des)
        }
        barChart.data = barData
    }
}

