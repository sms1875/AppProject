package com.example.application.ui.Home.Menu.HopeClass


import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.application.*
import com.example.application.databinding.FragmentHopeClassDialogBinding


class HopeClassDialogFragment : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentHopeClassDialogBinding? = null
    private val binding get() = _binding!!

    private var spinnerCity: Spinner? = null
    private var spinnerSigungu: Spinner? = null
    private var arrayAdapter: ArrayAdapter<String>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHopeClassDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var person=""
        var day = ""
        var hopedClassTime =""
        var isOn =""
        var isOff =""
        var type =""
        var region=""

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

        binding.btnHopeClassLike.setOnClickListener {
            person =when(binding.hopeClassPostNumGroup.checkedRadioButtonId){
                com.example.application.R.id.cb_hopeClassPostNum1->"1,0,0,0"
                com.example.application.R.id.cb_hopeClassPostNum2->"0,1,0,0"
                com.example.application.R.id.cb_hopeClassPostNum3->"0,0,1,0"
                com.example.application.R.id.cb_hopeClassPostNum4->"0,0,0,1"
                else -> {""}
            }
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

            when(binding.hopeClassPostTypeGroup.checkedRadioButtonId){
                com.example.application.R.id.cb_hopeClassPostType1->type ="0"
                com.example.application.R.id.cb_hopeClassPostType2->type="1"
                else -> {}
            }

            with(binding.spinnerHopeClassPostRegionselect1){
                when(selectedItemPosition){
                    0 -> region+="seoul_"
                    1 -> region+="busan_"
                    2 -> region+="daegu_"
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
                com.example.application.R.id.cb_hopeClassPostOnOffLine1-> {
                    isOn ="1"
                    isOff ="0"
                    region=""
                }
                com.example.application.R.id.cb_hopeClassPostOnOffLine2->{
                    isOn ="0"
                    isOff ="1"
                }
                else -> {}
            }

            mDialogResult?.finish(person,day,hopedClassTime,isOn,isOff,type,region)
            dialog!!.dismiss()
        }
        binding.btnHopeClassLikeCancle.setOnClickListener {
            dialog!!.dismiss()
        }

        setCancelable(false);
        spinnerCity = binding.spinnerHopeClassPostRegionselect1
        arrayAdapter = ArrayAdapter(requireContext(),  android.R.layout.simple_spinner_item, resources.getStringArray(R.array.region))
        arrayAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity!!.adapter = arrayAdapter
        spinnerSigungu = binding.spinnerHopeClassPostRegionselect2
        initAddressSpinner()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth )
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        //사이즈 조절
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

    var mDialogResult: OnMyDialogResult? = null

    fun setDialogResult(dialogResult: OnMyDialogResult) {
        mDialogResult = dialogResult
    }

    interface OnMyDialogResult {
        fun finish(person:String,day:String,hopedClassTime:String,isOn:String,isOff:String,type:String,region:String)
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

    override fun onClick(v: View) {

    }

}
