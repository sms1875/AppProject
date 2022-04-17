package com.example.application.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
import com.example.application.*
import com.example.application.Class.ClassInfo.ClassInfoActivity
import com.example.application.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.POST


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeFragmentViewModel
    private var isRunning = true

    lateinit var deadLineClassAdapter: ClassAdapter
    lateinit var newClassAdapter: ClassAdapter

    private var deadlineClassList = mutableListOf<classData>()
    private var newClassList = mutableListOf<classData>()

    private var bannerList = listOf(
        ("branding/googlelogo/1x/googlelogo_color_272x92dp.png"),
        ("branding/googlelogo/1x/googlelogo_color_272x92dp.png"),
        ("branding/googlelogo/1x/googlelogo_color_272x92dp.png"),
    )

    lateinit var bannerAdapter: BannerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TOKEN = SharedPreferences.prefs.getString("key","key")
        val getClassService = ServiceGenerator.createService(getClassInterface::class.java,TOKEN)

        getClassService.getClassList().enqueue(object : Callback<classDataList> {
            override fun onResponse(call: Call<classDataList>, response: Response<classDataList>) {
                deadlineClassList= response.body()?.classData as ArrayList<classData>
                Log.d("home", "성공 : ${deadlineClassList}")
                initRecycler()

            }
            override fun onFailure(call: Call<classDataList>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
            }
        })

        var classData1=classData("test","tutor","5000","","id","1")
        var classData2=classData("name","tutor","4000","","i2","2")
        var classD=listOf(classData1,classData2)



        with(binding.rvBanner){
            bannerAdapter = BannerAdapter(bannerList as MutableList<String>)
            adapter=bannerAdapter
            setCurrentItem(bannerList.size*10,false)
            binding.textView.text = "/${bannerList.size}"
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    if (positionOffsetPixels == 0) {
                        setCurrentItem(position)
                    }
                }
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    isRunning=true
                    binding.tvPageNumber.text = "${position%bannerList.size+1}"
                    viewModel.setCurrentPosition(position)
                }
            })
            bannerAdapter.setOnItemclickListner(object: BannerAdapter.OnItemClickListner{
                override fun onItemClick(view: View, position: Int, data: String) {
                    Log.d("banner","test ${position}")
                }
            })
            subscribeObservers()
            autoScrollViewPager()
        }

        binding.btnNotice.setOnClickListener {
            Toast.makeText(requireContext(), "구현중입니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initRecycler() {
        deadLineClassAdapter = ClassAdapter(requireContext())
        binding.rvHomeDeadlineClass.adapter = deadLineClassAdapter
        binding.rvHomeDeadlineClass.addItemDecoration(VerticalItemDecorator(20))
        binding.rvHomeDeadlineClass.addItemDecoration(HorizontalItemDecorator(10))

        deadLineClassAdapter.datas = deadlineClassList
        deadLineClassAdapter.notifyDataSetChanged()
        deadLineClassAdapter.setOnItemClickListener(object :ClassAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: classData, pos : Int) {
                Intent(context, ClassInfoActivity::class.java).apply {
                    this.putExtra("classID",data.classId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })

        newClassAdapter = ClassAdapter(requireContext())
        binding.rvHomeNewClass.adapter = newClassAdapter
        binding.rvHomeNewClass.addItemDecoration(VerticalItemDecorator(20))
        binding.rvHomeNewClass.addItemDecoration(HorizontalItemDecorator(10))

        newClassAdapter.datas = deadlineClassList
        newClassAdapter.notifyDataSetChanged()
        newClassAdapter.setOnItemClickListener(object :ClassAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: classData, pos : Int) {
                Intent(context, ClassInfoActivity::class.java).apply {
                    this.putExtra("classID",data.classId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })

    }

    private fun subscribeObservers() {
        viewModel.bannerItemList.observe(viewLifecycleOwner, Observer { bannerItemList ->
            bannerAdapter.submitList(bannerList)
        })
        viewModel.currentPosition.observe(viewLifecycleOwner, Observer { currentPosition ->
            binding.rvBanner.currentItem = currentPosition
        })
    }

    private fun autoScrollViewPager() {
        lifecycleScope.launchWhenResumed {
            while (isRunning) {
                delay(3000)
                viewModel.getcurrentPosition()?.let {
                    viewModel.setCurrentPosition((it.plus(1)) % bannerList.size)
                }
            }
        }
    }
}


interface getClassInterface {
    @POST("/db/class/get")
    fun getClassList(): Call<classDataList>
}//로그인 인터페이스

