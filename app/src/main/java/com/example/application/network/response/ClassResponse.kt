package com.example.application.network.response

import com.google.gson.annotations.SerializedName


data class classData(
    @SerializedName("className") var className: String="",
    @SerializedName("classTutor") var classTutor: String="",
    @SerializedName("classPrice") var classPrice: String="",
    @SerializedName("classImage") var classImage: String="",
    @SerializedName("classId") var classId: String="",
    @SerializedName("classOnOffline") var classOnOffline: String="",
)

data class classDataList (
    @SerializedName("classData") val classData: List<classData>,
)



/***************강의 목록 데이터*********************/

data class classDetailInfo(
    @SerializedName("className") var className: String="",
    @SerializedName("classTutorName") var classTutorName: String="",
    @SerializedName("classCategory") var classCategory: String="",
    @SerializedName("classPrice") var classPrice: String="",
    @SerializedName("classImage") val classImage: String="",
    @SerializedName("classInfo") var classInfo: String="",
    @SerializedName("classId") val classId: String="",
    @SerializedName("start_date") val start_date: String,
    @SerializedName("end_date") val end_date: String,
    @SerializedName("start_time") val start_time: String,
    @SerializedName("end_time") val end_time: String,
    @SerializedName("classAddress") val classAddress: String,
    @SerializedName("classReviewScore") val classReviewScore: String="",
    @SerializedName("classReviewCount") var classReviewCount: String=""
)

/***************강의 정보 확인 데이터*********************/

data class QnAPostList(
    @SerializedName("QnAId") var QnAId: String="",
    @SerializedName("QnAgroupId") var QnAgroupId: String="",
    @SerializedName("QnAauther") var QnAauther: String="",
    @SerializedName("QnAtitle") var QnAtitle: String="",
    @SerializedName("QnAdate") var QnAdate: String=""
)


/***************강의 정보 유저 리뷰 데이터*********************/

data class userReview(
    @SerializedName("userId") var userId: String="",
    @SerializedName("reviewContent") var reviewContent: String="",
    @SerializedName("reviewScore") var reviewScore: String=""
)

data class userReviewList(
    @SerializedName("reviewData") val reviewData: List<userReview>,
)

