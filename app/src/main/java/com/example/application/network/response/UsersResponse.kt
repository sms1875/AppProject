package com.example.application.network.response

import com.google.gson.annotations.SerializedName
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


data class scheduleDataList (
    @SerializedName("openedClass") val opendClass: List<scheduleData>,  // 개설한 강의
    @SerializedName("registeredClass") val registeredClass: List<scheduleData>,  // 수강중인 강의
)
data class scheduleData(
    @SerializedName("className") val className: String ,
    @SerializedName("start_date") val start_date: String ,
    @SerializedName("end_date") val end_date: String,
    @SerializedName("start_time") val start_time: String ,
    @SerializedName("end_time") val end_time: String
)
data class schedulelist(
    @SerializedName("schedule") val schedule: String,
    @SerializedName("date") val date: Date
)

data class instructorInfo(
    @SerializedName("licenseInfo") val licenseInfo: String,
    @SerializedName("historyInfo") val historyInfo: String,
    @SerializedName("areaInfo") val areaInfo: String,
    @SerializedName("selfIntroduce") val selfIntroduce: String
)
