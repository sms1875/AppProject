package com.example.application.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
