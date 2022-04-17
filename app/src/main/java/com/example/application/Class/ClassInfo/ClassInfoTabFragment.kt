package com.example.application.Class.ClassInfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.application.classDetailInfo
import com.example.application.databinding.FragmentClassInfoTabBinding



class ClassInfoTabFragment : Fragment() {

    private var _binding: FragmentClassInfoTabBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstant(ClassInfo: classDetailInfo) : ClassInfoTabFragment {
            val fragment = ClassInfoTabFragment()
            val args = Bundle()
            args.putString("category", ClassInfo.classCategory)
            args.putString("price", ClassInfo.classPrice)
            args.putString("info", ClassInfo.classInfo)
            args.putString("address", ClassInfo.classAddress)
            args.putString("start_date", ClassInfo.start_date)
            args.putString("end_date", ClassInfo.end_date)
            args.putString("start_time", ClassInfo.start_time)
            args.putString("end_time", ClassInfo.end_time)

            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassInfoTabBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtClassCategory.text = String.format("Category : %s ", arguments?.getString("category")) //문자열 리스트 분리 필요
        binding.txtClassPrice.text = String.format("Price : %s ", arguments?.getString("price"))
        binding.txtClassAddress.text = String.format("Address : %s ", arguments?.getString("address"))
        binding.txtClassDate.text = String.format("Date : %s  ~  %s ", arguments?.getString("start_date"),  arguments?.getString("end_date"))
        binding.txtClassTime.text = String.format("Time : %s  ~  %s ", arguments?.getString("start_time"),  arguments?.getString("end_time"))

        binding.txtClassInfo.text = arguments?.getString("info")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}