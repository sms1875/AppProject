package com.example.application.network.service

import com.example.application.network.response.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface singInterface {
    @FormUrlEncoded
    @POST("/users/login")
    fun login(@Field("userid") userid:String,
              @Field("password") pw:String
    ) : Call<loginResponse>
}//로그인 인터페이스

interface tokenInterface {
    @FormUrlEncoded
    @POST("/users/registerToken")
    fun token(@Field("userToken") userToken:String
    ) : Call<resultResponse>
}//로그인 인터페이스

interface registerInterface {
    @FormUrlEncoded
    @POST("/users/addUser")
    fun register(
        @Field("userid") userid: String,
        @Field("password") password : String,
        @Field("usertel") usertel : String,
        @Field("username") username: String,
    ) : Call<resultResponse>
}//회원가입 인터페이스

interface idConfirmInterface {
    @FormUrlEncoded
    @POST("/users/confirm")
    fun idConfirm(
        @Field("id") id: String
    ) : Call<resultResponse>
}//아이디 중복확인 인터페이스

interface instructorInfoEditInterface {
    @FormUrlEncoded
    @POST("/user/tutor수정")
    fun editInstructorInfo(
        @Field("licenseInfo") licenseInfo : String,
        @Field("historyInfo") historyInfo : String,
        @Field("areaInfo") areaInfo : String,
        @Field("selfIntroduce") selfIntroduce: String,
    ) : Call<resultResponse>
}//강사정보 수정


interface getInstructorInfoInterface {
    @POST("/user/tutor수정")
    fun getInstructorInfo(): Call<instructorInfo>
}//강사정보 받아오기


interface instructorRegisterInterface {
    @FormUrlEncoded
    @POST("/user/tutorRegister")
    fun register(
        @Field("licenseInfo") licenseInfo : String,
        @Field("historyInfo") historyInfo : String,
        @Field("areaInfo") areaInfo : String,
        @Field("selfIntroduce") selfIntroduce: String,
    ) : Call<resultResponse>
}//강사등록신청

interface getMyInfoInterface {
    @POST("/user/myInfo")
    fun getMyInfo() : Call<GetMyInfoResponse>
}//아이디 중복확인 인터페이스


interface updateInfoInterface {
    @FormUrlEncoded
    @POST("/user/updateInfo")
    fun updateInfo(
        @Field("userName") userName: String,
        @Field("userTel") userTel : String
    ) : Call<updateInfoResponse>
}//회원가입 인터페이스
