package com.example.application.ui.Class.ClassInfo.QnA

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.application.*
import com.example.application.databinding.FragmentClassQnaTabBinding
import com.example.application.network.response.*
import com.example.application.network.service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassQnATab : Fragment() {

    private var _binding: FragmentClassQnaTabBinding? = null
    private val binding get() = _binding!!

    //lateinit var classQnAAdapter: ClassQnAAdapter

    lateinit var QnAPostList: List<QnAPost>

    companion object {
        fun newInstant(ClassInfo: classDetailInfo): ClassQnATab {
            val fragment = ClassQnATab()
            val args = Bundle()
            args.putString("classReviewCount", ClassInfo.classReviewCount)
            args.putString("classReviewScore", ClassInfo.classReviewScore)
            args.putString("classId", ClassInfo.classId)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassQnaTabBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.d("111111111111111111111", arguments.toString())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TOKEN=SharedPreferences.prefs.getString("key","key")

        val classQNARegisterService = ServiceGenerator.createService(classQNARegisterInterface::class.java)
        val getClassQNAListService = ServiceGenerator.createService(getClassQNAListInterface::class.java,TOKEN)
        val getClassQNAInfoService = ServiceGenerator.createService(getClassQNAInfoInterface::class.java,TOKEN)

        var searchCategory=""//검색옵션
        with(binding.spinnerClassQnASearchOption) {
            val typeList=resources.getStringArray(com.example.application.R.array.searchOption)
            val searchOptionAdapter = SearchOptionAdapter(context, android.R.layout.simple_spinner_dropdown_item, typeList)
            adapter = searchOptionAdapter
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(position){
                        0-> searchCategory=""
                        1-> searchCategory="제목"
                        2-> searchCategory="내용"
                        3-> searchCategory="작성자"
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }//검색 옵션 설정
        }



        val data=QnAPost("22","6test",46,null)

        binding.btnClassQnAPost.setOnClickListener {

/*
            with(classQNARegisterService) {
                QNAregister(
                    data.Title,
                    data.Content,
                    data.targetClassId,
                    data.parentDocumentId
                ).enqueue(object :
                    Callback<resultResponse> {
                    override fun onResponse(
                        call: Call<resultResponse>,
                        response: Response<resultResponse>
                    ) {
                        android.util.Log.e("성공", "${response}")
                    }

                    override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                        android.util.Log.e("실패", "${t}")
                    }
                })*/
/*
            with(getClassQNAListService) {

                getQNAList(data.targetClassId,1).enqueue(object : Callback<QnAData> {
                    override fun onResponse(call: Call<QnAData>, response: Response<QnAData>) {
                        android.util.Log.e("성공", "${response.body()}")
                    }
                    override fun onFailure(call: Call<QnAData>, t: Throwable) {
                        android.util.Log.e("실패", "${t}")
                    }
                })
*/
            with(getClassQNAInfoService) {

                getQNAInfo(1).enqueue(object : Callback<QnADeInfo> {
                    override fun onResponse(call: Call<QnADeInfo>, response: Response<QnADeInfo>) {
                        android.util.Log.e("성공", "${response.body()}")
                    }
                    override fun onFailure(call: Call<QnADeInfo>, t: Throwable) {
                        android.util.Log.e("실패", "${t}")
                    }
                })

            }
        }

        //initRecycler()//recyclerview 설정
/*
            val getReviewClassService = ServiceGenerator.createService(getReviewClassInterface::class.java)



            var max_page=(
                    if (arguments?.getString("classReviewCount")!!.toInt()!=0){
                        arguments?.getString("classReviewCount")!!.toInt()/10+1
                    }
                    else 1
                    )
            binding.lpbButtonlist.setPageItemCount(5)
            binding.lpbButtonlist.addBottomPageButton(max_page,1)
            with(getReviewClassService) {
                getReviewClass(arguments?.getString("classId"),1.toString()).enqueue(object : Callback<userReviewList> {
                    override fun onResponse(call: Call<userReviewList>, response: Response<userReviewList>) {
                        Log.d("1341241234", response.body().toString())
                        userReviewList=response.body()?.reviewData as ArrayList<userReview>
                        initRecycler()//recyclerview 설정
                    }
                    override fun onFailure(call: Call<userReviewList>, t: Throwable) {
                        Log.e("실패", "${t}")
                    }
                })
            }
            binding.lpbButtonlist.setOnPageSelectListener(object : OnPageSelectListener {
                override fun onPageBefore(now_page: Int) {
                    binding.lpbButtonlist.addBottomPageButton(max_page, now_page)

                    with(getReviewClassService) {
                        getReviewClass(arguments?.getString("classId"),now_page.toString()).enqueue(object : Callback<userReviewList> {
                            override fun onResponse(call: Call<userReviewList>, response: Response<userReviewList>) {
                                Log.d("bbbbbbbbbbbbbbbbbbbb", response.body().toString())
                                userReviewList=response.body()?.reviewData as ArrayList<userReview>
                                initRecycler()//recyclerview 설정
                            }
                            override fun onFailure(call: Call<userReviewList>, t: Throwable) {
                                Log.e("실패", "${t}")
                            }
                        })
                    }

                    initRecycler()//recyclerview 설정
                }


                override fun onPageCenter(now_page: Int) {

                    with(getReviewClassService) {
                        getReviewClass(arguments?.getString("classId"),now_page.toString()).enqueue(object : Callback<userReviewList> {
                            override fun onResponse(call: Call<userReviewList>, response: Response<userReviewList>) {
                                Log.d("aaaaaaaaaaaaa", response.body().toString())
                                userReviewList=response.body()?.reviewData as ArrayList<userReview>
                                initRecycler()//recyclerview 설정
                            }
                            override fun onFailure(call: Call<userReviewList>, t: Throwable) {
                                Log.e("실패", "${t}")
                            }
                        })
                    }
                }

                override fun onPageNext(now_page: Int) {
                    binding.lpbButtonlist.addBottomPageButton(max_page, now_page)

                    with(getReviewClassService) {
                        getReviewClass(arguments?.getString("classId"),now_page.toString()).enqueue(object : Callback<userReviewList> {
                            override fun onResponse(call: Call<userReviewList>, response: Response<userReviewList>) {
                                Log.d("1341241234", response.body().toString())
                                userReviewList=response.body()?.reviewData as ArrayList<userReview>
                                initRecycler()//recyclerview 설정
                            }
                            override fun onFailure(call: Call<userReviewList>, t: Throwable) {
                                Log.e("실패", "${t}")
                            }
                        })
                    }

                }

            })
            //리뷰 데이터
            val Score= arguments?.getString("classReviewScore")
            if (Score != null) {
                binding.reviewRatingBar.rating= Score.toFloat()
                binding.txtClassReviewScore.text = Score
            }//rating 값으로 별 개수 설정
            else{
                binding.reviewRatingBar.isEnabled=false
                binding.reviewRatingBar.visibility=View.INVISIBLE
                binding.txtClassReviewScore.text = "리뷰가 없습니다 !"
            }
            binding.txtClassReviewCount.text = String.format("%s 리뷰",arguments?.getString("classReviewCount") )
*/
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
/*
       private fun initRecycler() {
           /assQnAAdapter = ClassQnAAdapter(requireContext())
           binding.rvClassQnA.adapter = classQnAAdapter
           binding.rvClassQnA.addItemDecoration(ClassReviewVerticalItemDecorator(20))
           binding.rvClassQnA.addItemDecoration(ClassReviewHorizontalItemDecorator(10))

           classQnAAdapter.datas = QnAPostList

   }*/
}
