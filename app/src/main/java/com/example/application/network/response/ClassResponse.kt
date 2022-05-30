package com.example.application.network.response

import com.google.gson.annotations.SerializedName
import java.time.format.DateTimeFormatter


data class classData(
    @SerializedName("className") var className: String="",
    @SerializedName("classTutor") var classTutor: String="",
    @SerializedName("classPrice") var classPrice: String="",
    @SerializedName("classImage") var classImage: String="",
    @SerializedName("classId") var classId: String="",
    @SerializedName("classOnOffline") var classOnOffline: String="",
    @SerializedName("minPerson") var minPerson: Int,
    @SerializedName("maxPerson") var maxPerson: Int,
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
    @SerializedName("classReviewCount") var classReviewCount: String="",
    @SerializedName("classTimeInfo") val classTimeInfo: classTimeList,
)

data class classTimeList(
    @SerializedName("mon") var mon: String,
    @SerializedName("tue") val tue: String,
    @SerializedName("wed") var wed: String,
    @SerializedName("thur") val thur: String,
    @SerializedName("fri") var fri: String,
    @SerializedName("sat") val sat: String,
    @SerializedName("sun") var sun: String
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

/***************강의 정보 확인 데이터*********************/

data class QnAPost(
    @SerializedName("Title") var Title: String="",
    @SerializedName("Content") var Content: String="",
    @SerializedName("targetClassId") var targetClassId: Int,
    @SerializedName("parentDocumentId") var parentDocumentId: Int? =null,
)

data class QnAData(
    @SerializedName("qnaData") val qnaData: List<QnAList>,
){
    data class QnAList(
        @SerializedName("count") var count: Int,
        @SerializedName("documentId") var documentId: Int,
        @SerializedName("writedtime") var writedtime: String,
        @SerializedName("writer") var writer: String,
        @SerializedName("documentTitle") var documentTitlte: String,
    )
}


data class QnADeInfo(
    @SerializedName("qnaData") val qnaData : qnaDataBody
) {
    data class qnaDataBody(
        @SerializedName("documentId") var documentId: Int,
        @SerializedName("writedtime") var writedtime: String,
        @SerializedName("writerId") var writerId: String,
        @SerializedName("documentTitle") var documentTitlte: String,
        @SerializedName("documentContent") var documentContent: String,
        @SerializedName("targetClassId") var targetClassId: Int,
        @SerializedName("parentDocumentId") var parentDocumentId: Int?,
        @SerializedName("isModified") var isModified: Int,
        @SerializedName("level") var level: Int,
        @SerializedName("view") var view: Int,
        @SerializedName("count") var count: Int,
    )
}
