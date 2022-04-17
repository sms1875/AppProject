package com.example.application.Home.Menu.HopeClass


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.application.*
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


        with(hopeClassPage) {
            binding.txtHopeClassPostInfoTitle.text = desiredTitle
            binding.txtHopeClassPostInfoCategory.text = category
            binding.txtClassInfo.text = classDesc

            BarChartGraph(binding.hopeClassPostInfoBarChart1,
                listOf(personInfo.underThree,personInfo.ThreeAndTen,personInfo.TenAndTwentyfive,personInfo.overTwentyfive),
                listOf("3명 이하","4~10명","11~25명","상관없음"))

            BarChartGraph(binding.hopeClassPostInfoBarChart2,
                listOf(timeInfo.Morning,timeInfo.Afternoon,timeInfo.Night,timeInfo.Overnight),
                listOf("오전","오후","저녁","심야"))

            BarChartGraph(binding.hopeClassPostInfoBarChart3,
                listOf(dateInfo.Monday,dateInfo.Tuesday,dateInfo.Wednesday,dateInfo.Thursday,dateInfo.Friday,dateInfo.Saturday,dateInfo.Sunday),
                listOf("월","화","수","목","금","토","일"))

            PieChartGraph(binding.hopeClassPostInfoPieChart1,
                listOf(typeInfo.Oneday,typeInfo.Regular),
                listOf("원데이 클래스","정규 강의"))

            PieChartGraph(binding.hopeClassPostInfoPieChart2,
                listOf(onOffInfo.online,onOffInfo.offline),
                listOf("온라인","오프라인"))

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
                                                    val toast = Toast.makeText(requireContext(), "개설되었습니다", Toast.LENGTH_SHORT)
                                                    toast.show()
                                                }//
                                                "fail" -> {
                                                    val toast = Toast.makeText(requireContext(), "개설에 실패했습니다", Toast.LENGTH_SHORT)
                                                    toast.show()
                                                }//
                                                "internal" -> {
                                                    val toast = Toast.makeText(requireContext(), "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT)
                                                    toast.show()
                                                }//
                                            }
                                        }
                                        override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                                            Log.d("결과:", "실패 : $t")
                                        }
                                    })
                                }
                            })
                        }

                    }
                }
                1->{
                    binding.btnLike.text="공감 취소"
                    binding.btnLike.setOnClickListener {
                        hopeClassTopicLikeCancelService.hopeClassTopicLikeCancel(topicId).enqueue(object : Callback<resultResponse> {
                            override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                                Log.d("결과:", "성공 : $response")
                            }
                            override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                                Log.d("결과:", "실패 : $t")
                            }
                        })//희망 목록 받아오기

                    }

                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //맵뷰 설정
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
                        region[0]="서울특별시"
                        val array = resources.getStringArray(R.array.region_seoul)
                        region[1]=array[region[1].toInt()]
                    }
                    "busan"->{
                        region[0]="부산광역시"
                        val array = resources.getStringArray(R.array.region_buesan)
                        region[1]=array[region[1].toInt()]
                    }
                    "daegu"->{
                        region[0]="대구광역시"
                        val array = resources.getStringArray(R.array.region_daegu)
                        region[1]=array[region[1].toInt()]
                    }
                    "incheon"->{
                        region[0]="인천광역시"
                        val array = resources.getStringArray(R.array.region_seoul)
                        region[1]=array[region[1].toInt()]
                    }
                    "gwangju"->{
                        region[0]="광주광역시"
                        val array = resources.getStringArray(R.array.region_gwangju)
                        region[1]=array[region[1].toInt()]
                    }
                    "daejeon"->{
                        region[0]="대전광역시"
                        val array = resources.getStringArray(R.array.region_daejeon)
                        region[1]=array[region[1].toInt()]
                    }
                    "ulsan"->{
                        region[0]="울산광역시"
                        val array = resources.getStringArray(R.array.region_ulsan)
                        region[1]=array[region[1].toInt()]
                    }
                    "sejong"->{
                        region[0]="세종특별자치시"
                        val array = resources.getStringArray(R.array.region_sejong)
                        region[1]=array[region[1].toInt()]
                    }
                    "gyeonggi"->{
                        region[0]="경기도"
                        val array = resources.getStringArray(R.array.region_gyeonggi)
                        region[1]=array[region[1].toInt()]
                    }
                    "gangwon"->{
                        region[0]="강원도"
                        val array = resources.getStringArray(R.array.region_gangwon)
                        region[1]=array[region[1].toInt()]
                    }
                    "chungbuk"->{
                        region[0]="충청북도"
                        val array = resources.getStringArray(R.array.region_chungbuk)
                        region[1]=array[region[1].toInt()]
                    }
                    "chungnam"->{
                        region[0]="충청남도"
                        val array = resources.getStringArray(R.array.region_chungnam)
                        region[1]=array[region[1].toInt()]
                    }
                    "jeonbuk"->{
                        region[0]="전라북도"
                        val array = resources.getStringArray(R.array.region_jeonbuk)
                        region[1]=array[region[1].toInt()]
                    }
                    "jeonnam"->{
                        region[0]="전라남도"
                        val array = resources.getStringArray(R.array.region_jeonnam)
                        region[1]=array[region[1].toInt()]
                    }
                    "gyeongbuk"->{
                        region[0]="경상북도"
                        val array = resources.getStringArray(R.array.region_gyeongbuk)
                        region[1]=array[region[1].toInt()]
                    }
                    "gyeongnam"->{
                        region[0]="경상남도"
                        val array = resources.getStringArray(R.array.region_gyeongnam)
                        region[1]=array[region[1].toInt()]
                    }
                    "jeju"->{
                        region[0]="제주특별자치도"
                        val array = resources.getStringArray(R.array.region_jeju)
                        region[1]=array[region[1].toInt()]
                    }
                }//지역 태그 분류
                val offsetItem = MyItem(data[i].placeX.toDouble(), data[i].placeY.toDouble(),region[0]+" "+region[1] , data[i].count+"명")
                clusterManager.addItem(offsetItem)
            }
        }
    }

    private fun PieChartGraph(PieChart: PieChart, valList: List<Int>, labelList:List<String>) {
        PieChart.setTouchEnabled(false)//터치 금지
        PieChart.setDrawEntryLabels(false)//라벨표시x

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
                return   "$num"+"명"
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
        barChart.setTouchEnabled(false)//터치 금지
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
                return   "$num"+"명"
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



interface hopeClassTopicLikeCancelInterface {
    @FormUrlEncoded
    @POST("/db/class/hopedClassLikeCancel")
    fun hopeClassTopicLikeCancel(
        @Field("topicId") topicId :String,
    ): Call<resultResponse>
}//공감취소



interface hopeClassTopicLikeInterface {
    @FormUrlEncoded
    @POST("/db/class/hopedClassLike")
    fun hopeClassTopicLike(
        @Field("topicId") topicId :String,
        @Field("person") person : String,
        @Field("day") day  : String,
        @Field("hopedClassTime") hopedClassTime : String,
        @Field("isOn") isOn : String,
        @Field("isOff") isOff : String,
        @Field("desiredPlace") desiredPlace : String,
        @Field("type") type : String,
    ): Call<resultResponse>
}//공감
