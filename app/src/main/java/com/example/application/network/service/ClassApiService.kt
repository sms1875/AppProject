package com.example.application.network.service

import com.example.application.network.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ClassCreateInterface {
    @Multipart
    @POST("/class/register")
    fun creatClass(
        @Part classImage: MultipartBody.Part,
        @PartMap data: HashMap<String, RequestBody>
    ): Call<resultResponse>
}

interface MyClassInterface {
    @POST("/class/getMyClass")
    fun getMyClassList(): Call<classDataList>
}//로그인 인터페이스


interface classSearchInterface {
    @FormUrlEncoded
    @POST("/class/search")
    fun classSearch(
        @Field("string") string:String,
        @Field("category") category:String,
        @Field("orderByString") orderByString:String,
    ): Call<classDataList>
}//강의 취소


interface getClassInterface {
    @POST("/class/get")
    fun getClassList(): Call<classDataList>
}//로그인 인터페이스


interface getClassDetailInterface {
    @FormUrlEncoded
    @POST("/class/detail")
    fun getClassDetail(@Field("classId") classId: String?
    ): Call<classDetailInfo>
}//로그인 인터페이스


interface singupClassInterface {
    @FormUrlEncoded
    @POST("/class/reservation")
    fun singupClass(@Field("classId") classId: String?
    ): Call<resultResponse>
}//로그인 인터페이스


interface cancelReservationClassInterface {
    @FormUrlEncoded
    @POST("/class/cancelReservation")
    fun cancelReservation(@Field("reservationId") reservationId: String?
    ): Call<resultResponse>
}//로그인 인터페이스


interface cancelRegisterClassInterface {
    @FormUrlEncoded
    @POST("/class/cancelRegister")
    fun singupClass(@Field("classId") classId: String?
    ): Call<resultResponse>
}//로그인 인터페이스

interface getReviewClassInterface {
    @FormUrlEncoded
    @POST("/class/getReview")
    fun getReviewClass(
        @Field("classId") classId: String?,
        @Field("pageNo") pageNo: String?
    ): Call<userReviewList>
}//로그인 인터페이스
interface getCompletedClassInterface {
    @POST("/class/getCompletedClass")
    fun getCompletedClass(): Call<classDataList>
}//completed강의 목록


interface reviewClassInterface {
    @FormUrlEncoded
    @POST("/class/review")
    fun reviewClass(
        @Field("classId") classId:String,
        @Field("reviewContent") reviewContent:String,
        @Field("reviewScore") reviewScore:String
    ): Call<resultResponse>
}


interface getScheduleInterface {
    @POST("/class/getList")
    fun getSchedule(): Call<scheduleDataList>
}

interface getMyOpenedClassInterface {
    @POST("/class/getMyOpenedClass")
    fun getMyOpenedClass(): Call<classDataList>
}//등록한 강의 목록

interface getMyOpenedCompletedClassInterface {
    @POST("/class/getMyOpenedCompletedClass")
    fun getMyOpenedCompletedClass(): Call<classDataList>
}//completed강의 목록

interface cancelClassRegisterInterface {
    @FormUrlEncoded
    @POST("/class/cancelRegister")
    fun cancelClassRegister(@Field("classId") classId:String,
    ): Call<resultResponse>
}//강의 취소

interface reopenClassInterface {
    @FormUrlEncoded
    @POST("/class/reopen")
    fun reopenClass(
        @Field("classId") classId:String,
        @Field("start_date") start_date:String,
        @Field("start_time") start_time:String,
        @Field("end_date") end_date:String,
        @Field("end_time") end_time:String
    ): Call<resultResponse>
}

interface classQNARegisterInterface {
    @FormUrlEncoded
    @POST("/qna/register")
    fun QNAregister(
        @Field("Title") Title:String,
        @Field("content") Content:String,
        @Field("targetClassId") targetClassId:Int,
        @Field("parentDocumentId") parentDocumentId: Int?,
    ): Call<resultResponse>
}


interface getClassQNAListInterface {
    @FormUrlEncoded
    @POST("/qna/get")
    fun getQNAList(
        @Field("targetClassId") targetClassId:Int,
        @Field("pageNo") pageNo:Int,
    ): Call<QnAData>
}

interface getClassQNAInfoInterface {
    @FormUrlEncoded
    @POST("/qna/getQna")
    fun getQNAInfo(
        @Field("documentId") documentId:Int,
    ): Call<QnADeInfo>
}



