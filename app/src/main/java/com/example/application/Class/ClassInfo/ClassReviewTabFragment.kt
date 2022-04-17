package com.example.application.Class.ClassInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.application.*
import com.example.application.databinding.FragmentClassReviewTabBinding
import com.lakue.pagingbutton.OnPageSelectListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


class ClassReviewTabFragment : Fragment() {

    private var _binding: FragmentClassReviewTabBinding? = null
    private val binding get() = _binding!!

    lateinit var classReviewAdapter: ClassReviewAdapter

    lateinit var userReviewList : List<userReview>

    companion object {
        fun newInstant(ClassInfo: classDetailInfo) : ClassReviewTabFragment {
            val fragment = ClassReviewTabFragment()
            val args = Bundle()
            args.putString("classReviewCount", ClassInfo.classReviewCount)
            args.putString("classReviewScore", ClassInfo.classReviewScore)
            args.putString("classId", ClassInfo.classId)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding  = FragmentClassReviewTabBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.d("111111111111111111111", arguments.toString())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        classReviewAdapter = ClassReviewAdapter(requireContext())
        binding.rvClassReview.adapter = classReviewAdapter
        binding.rvClassReview.addItemDecoration(ClassReviewVerticalItemDecorator(20))
        binding.rvClassReview.addItemDecoration(ClassReviewHorizontalItemDecorator(10))

        classReviewAdapter.datas = userReviewList
    }
}

interface getReviewClassInterface {
    @FormUrlEncoded
    @POST("/db/class/getReview")
    fun getReviewClass(
        @Field("classId") classId: String?,
        @Field("pageNo") pageNo: String?
    ): Call<userReviewList>
}//로그인 인터페이스
