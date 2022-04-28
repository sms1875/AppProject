package com.example.application

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import retrofit2.http.Field
import java.util.*


data class loginResponse(
    @SerializedName("result") val result: String,
    @SerializedName("token") val token: String,
    @SerializedName("userType") val userType: String,
    @SerializedName("userName") val userName: String
)

data class resultResponse(
    @SerializedName("result") val result: String
)

data class instructorInfo(
    @SerializedName("licenseInfo") val licenseInfo: String,
    @SerializedName("historyInfo") val historyInfo: String,
    @SerializedName("areaInfo") val areaInfo: String,
    @SerializedName("selfIntroduce") val selfIntroduce: String
)

/***************로그인 관련 응답 형식*********************/

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

data class dayTime(
    @SerializedName("day") var date: String="",
    @SerializedName("startTime") var startTime: String="",
    @SerializedName("endTime") var endTime: String="",
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



/***************QNA*********************/
data class hopeClassTopic(
    @SerializedName("desiredTitle") var desiredTitle: String="",
    @SerializedName("category") var category: String="",
    @SerializedName("desiredClassLike") var desiredClassLike: String="",
    @SerializedName("isOn") var isOn: String="",
    @SerializedName("type") var type: String="",
    @SerializedName("topicId") var topicId: String=""
)

@Parcelize
data class hopeClassPage(
    @SerializedName("desiredTitle") var desiredTitle: String="",
    @SerializedName("category") var category: String="",
    @SerializedName("classDesc") var classDesc: String="",
    @SerializedName("personInfo") var personInfo: hopeClassPersonInfo,
    @SerializedName("timeInfo") var timeInfo: hopeClassTimeInfo,
    @SerializedName("onOffInfo") var onOffInfo: hopeClassOnOffInfo,
    @SerializedName("placeInfo") var placeInfo: List<hopeClassPlaceInfo>,
    @SerializedName("dateInfo") var dateInfo: hopeClassDateInfo,
    @SerializedName("typeInfo") var typeInfo: hopeClassTypeInfo,
    @SerializedName("registered") var registered: Int,
) : Parcelable
@Parcelize
data class hopeClassPersonInfo(
    @SerializedName("underThree") var underThree: Int,
    @SerializedName("ThreeAndTen") var ThreeAndTen: Int,
    @SerializedName("TenAndTwentyfive") var TenAndTwentyfive: Int,
    @SerializedName("overTwentyfive") var overTwentyfive: Int,
): Parcelable
@Parcelize
data class hopeClassTimeInfo(
    @SerializedName("Morning") var Morning: Int,
    @SerializedName("Afternoon") var Afternoon: Int,
    @SerializedName("Night") var Night: Int,
    @SerializedName("Overnight") var Overnight:Int,
): Parcelable
@Parcelize
data class hopeClassOnOffInfo(
    @SerializedName("online") var online: Int,
    @SerializedName("offline") var offline: Int,
): Parcelable
@Parcelize
data class hopeClassPlaceInfo(
    @SerializedName("desiredPlace") var desiredPlace: String="",
    @SerializedName("count") var count: String="",
    @SerializedName("placeX") var placeX: String="",
    @SerializedName("placeY") var placeY:String="",
): Parcelable
@Parcelize
data class hopeClassDateInfo(
    @SerializedName("Monday") var Monday: Int,
    @SerializedName("Tuesday") var Tuesday: Int,
    @SerializedName("Wednesday") var Wednesday: Int,
    @SerializedName("Thursday") var Thursday:Int,
    @SerializedName("Friday") var Friday: Int,
    @SerializedName("Saturday") var Saturday: Int,
    @SerializedName("Sunday") var Sunday: Int,
): Parcelable
@Parcelize
data class hopeClassTypeInfo(
    @SerializedName("Regular") var Regular: Int,
    @SerializedName("Oneday") var Oneday: Int,
): Parcelable
/***************강의 정보 유저 리뷰 데이터*********************/

data class bannerList(
    @SerializedName("bannerList") val bannerList: List<String>,
)

/***************배너 데이터*********************/
@Parcelize
data class placeData(
    @SerializedName("name") val name: String,
    @SerializedName("position") val position: String,
    @SerializedName("type") val type: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("cost") val cost: String,
    @SerializedName("start_time") val start_time: String,
    @SerializedName("end_time") val end_time: String,
    @SerializedName("placeImage") val placeImage: String,
    @SerializedName("_id") val _id: String,

): Parcelable

data class placeDataList (
    @SerializedName("space") val space: List<placeData>,
)


data class GetMyInfoResponse(
    @SerializedName("result") val result: String,
    @SerializedName("tel") val tel: String,
    @SerializedName("usertype") val usertype: String,
    @SerializedName("username") val username: String
)

data class updateInfoResponse(
    @SerializedName("result") val result: String,
    @SerializedName("usertype") val usertype: String
)

data class scheduleData(
    @SerializedName("className") val className: String ,
    @SerializedName("start_date") val start_date: String ,
    @SerializedName("end_date") val end_date: String,
    @SerializedName("start_time") val start_time: String ,
    @SerializedName("end_time") val end_time: String
)

data class scheduleDataList (
    @SerializedName("openedClass") val opendClass: List<scheduleData>,  // 개설한 강의
    @SerializedName("registeredClass") val registeredClass: List<scheduleData>,  // 수강중인 강의
)


data class schedulelist(
    @SerializedName("schedule") val schedule: String,
    @SerializedName("date") val date: Date
)