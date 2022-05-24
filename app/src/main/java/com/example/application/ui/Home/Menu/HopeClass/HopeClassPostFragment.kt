package com.example.application.ui.Home.Menu.HopeClass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.application.*
import com.example.application.databinding.FragmentHopeClassPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import com.example.application.network.service.*
import com.example.application.network.response.*
class HopeClassPostFragment : Fragment() {

    private var _binding: FragmentHopeClassPostBinding? = null
    private val binding get() = _binding!!

    private var spinnerCity: Spinner? = null
    private var spinnerSigungu: Spinner? = null
    private var arrayAdapter: ArrayAdapter<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding  = FragmentHopeClassPostBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cbHopeClassPostOnOffLine2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.cbHopeClassPostOnOffLine2.isChecked==true) {
                binding.hopeClassPostRegionLayout.visibility = View.VISIBLE
                binding.hopeClassPostRegionLayout.isEnabled = true
            }
            else {
                binding.hopeClassPostRegionLayout.visibility = View.GONE
                binding.hopeClassPostRegionLayout.isEnabled = false
            }
        }//오프라인 지역 표시

        val TOKEN= SharedPreferences.prefs.getString("key","key")
        val HopeClassPostService = ServiceGenerator.createService(HopeClassPostInterface::class.java,TOKEN)

        binding.btnHopeClassPost.setOnClickListener {
            var category:String = when(binding.hopeClassPostCategoryGroup.checkedRadioButtonId){
                R.id.cb_hopeClassPostCategory1->"A"
                R.id.cb_hopeClassPostCategory2->"B"
                R.id.cb_hopeClassPostCategory3->"C"
                R.id.cb_hopeClassPostCategory4->"D"
                else -> {""}
            }
            var desiredTitle =binding.editHopeClassPostTitle.text.toString()
            var person:String =when(binding.hopeClassPostNumGroup.checkedRadioButtonId){
                R.id.cb_hopeClassPostNum1->"1,0,0,0"
                R.id.cb_hopeClassPostNum2->"0,1,0,0"
                R.id.cb_hopeClassPostNum3->"0,0,1,0"
                R.id.cb_hopeClassPostNum4->"0,0,0,1"
                else -> {""}
            }
            var day = ""
            with(day){
                if (binding.cbHopeClassPostDate1.isChecked==true) day+="1,"
                else  day+="0,"
                if (binding.cbHopeClassPostDate2.isChecked==true) day+="1,"
                else day+="0,"
                if (binding.cbHopeClassPostDate3.isChecked==true) day+="1,"
                else day+="0,"
                if (binding.cbHopeClassPostDate4.isChecked==true) day+="1,"
                else day+="0,"
                if (binding.cbHopeClassPostDate5.isChecked==true) day+="1,"
                else day+="0,"
                if (binding.cbHopeClassPostDate6.isChecked==true) day+="1,"
                else day+="0,"
                if (binding.cbHopeClassPostDate7.isChecked==true) day+="1"
                else day+="0"
            }

            var hopedClassTime =""
            with(hopedClassTime){
                if (binding.cbHopeClassPostTime1.isChecked==true) hopedClassTime+="1,"
                else hopedClassTime+="0,"
                if (binding.cbHopeClassPostTime2.isChecked==true) hopedClassTime+="1,"
                else hopedClassTime+="0,"
                if (binding.cbHopeClassPostTime3.isChecked==true) hopedClassTime+="1,"
                else hopedClassTime+="0,"
                if (binding.cbHopeClassPostTime4.isChecked==true) hopedClassTime+="1"
                else hopedClassTime+="0"
            }

            var isOn =""
            var isOff =""
            var type =""
            when(binding.hopeClassPostTypeGroup.checkedRadioButtonId){
                R.id.cb_hopeClassPostType1->type ="0"
                R.id.cb_hopeClassPostType2->type="1"
                else -> {}
            }
            var region=""
            var desiredClassDesc =binding.editHopeClassPostDescription.text.toString()

            with(binding.spinnerHopeClassPostRegionselect1){
                when(selectedItemPosition){
                    0 -> region+="seoul_"
                    1 -> region+="busan_"
                    2->region+="daegu_"
                    3 -> region+="incheon_"
                    4 -> region+="gwangju_"
                    5->region+="daejeon_"
                    6 -> region+="ulsan_"
                    7 -> region+="sejong_"
                    8->region+="gyeonggi_"
                    9-> region+="gangwon_"
                    10 -> region+="chungbuk_"
                    11->region+="chungnam_"
                    12 -> region+="jeonbuk_"
                    13 -> region+="jeonnam_"
                    14->region+="gyeongbuk_"
                    15-> region+="gyeongnam_"
                    16 -> region+="jeju_"
                }
                region+=(binding.spinnerHopeClassPostRegionselect2.selectedItemPosition+1).toString()
            }

            when(binding.hopeClassPostOnOffLineGroup.checkedRadioButtonId){
                R.id.cb_hopeClassPostOnOffLine1-> {
                    isOn ="1"
                    isOff ="0"
                    region=""
                }
                R.id.cb_hopeClassPostOnOffLine2->{
                    isOn ="0"
                    isOff ="1"
                }
                else -> {}
            }
            HopeClassPostService.hopeClassPost(desiredTitle, category, person, day, hopedClassTime, isOn, isOff, region, desiredClassDesc, type
            ).enqueue(object : Callback<resultResponse> {
                override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                    val result = response.body()
                    when (result?.result) {
                        "success" -> {
                            val toast = Toast.makeText(requireContext(), "개설되었습니다", Toast.LENGTH_SHORT)
                            toast.show()
                            val fragmentManager: FragmentManager = activity!!.supportFragmentManager
                            fragmentManager.beginTransaction().remove(this@HopeClassPostFragment)
                                .commit()
                            fragmentManager.popBackStack()
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

        spinnerCity = binding.spinnerHopeClassPostRegionselect1
        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, resources.getStringArray(R.array.region))
        arrayAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity!!.adapter = arrayAdapter
        spinnerSigungu = binding.spinnerHopeClassPostRegionselect2
        initAddressSpinner()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun initAddressSpinner() {
        spinnerCity!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                // 시군구, 동의 스피너를 초기화한다.
                when (position) {
                    0 -> setSigunguSpinnerAdapterItem(R.array.region_seoul)
                    1 -> setSigunguSpinnerAdapterItem(R.array.region_buesan)
                    2 -> setSigunguSpinnerAdapterItem(R.array.region_daegu)
                    3 -> setSigunguSpinnerAdapterItem(R.array.region_incheon)
                    4 -> setSigunguSpinnerAdapterItem(R.array.region_gwangju)
                    5 -> setSigunguSpinnerAdapterItem(R.array.region_daejeon)
                    6 -> setSigunguSpinnerAdapterItem(R.array.region_ulsan)
                    7 -> setSigunguSpinnerAdapterItem(R.array.region_sejong)
                    8 -> setSigunguSpinnerAdapterItem(R.array.region_gyeonggi)
                    9 -> setSigunguSpinnerAdapterItem(R.array.region_gangwon)
                    10 -> setSigunguSpinnerAdapterItem(R.array.region_chungbuk)
                    11 -> setSigunguSpinnerAdapterItem(R.array.region_chungnam)
                    12 -> setSigunguSpinnerAdapterItem(R.array.region_jeonbuk)
                    13 -> setSigunguSpinnerAdapterItem(R.array.region_jeonnam)
                    14 -> setSigunguSpinnerAdapterItem(R.array.region_gyeongbuk)
                    15 -> setSigunguSpinnerAdapterItem(R.array.region_gyeongnam)
                    16 -> setSigunguSpinnerAdapterItem(R.array.region_jeju)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setSigunguSpinnerAdapterItem(array_resource: Int) {
        if (arrayAdapter != null) {
            spinnerSigungu!!.adapter = null
            arrayAdapter = null
        }
        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, resources.getStringArray(array_resource) as Array<String>)
        arrayAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSigungu!!.adapter = arrayAdapter
    }

}
