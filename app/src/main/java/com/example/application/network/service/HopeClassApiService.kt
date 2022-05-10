package com.example.application.network.service

import com.example.application.network.response.hopeClassPage
import com.example.application.network.response.hopeClassTopic
import com.example.application.network.response.resultResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface HopeClassPostInterface {
    @FormUrlEncoded
    @POST("/class/hopedclassRegister")
    fun hopeClassPost(
        @Field("desiredTitle") desiredTitle : String,
        @Field("category") category : String,
        @Field("person") person : String,
        @Field("day") day  : String,
        @Field("hopedClassTime") hopedClassTime : String,
        @Field("isOn") isOn : String,
        @Field("isOff") isOff : String,
        @Field("desiredPlace") desiredPlace : String,
        @Field("desiredClassDesc") desiredClassDesc : String,
        @Field("type") type : String,
    ) : Call<resultResponse>
}//희망강의 topic post

interface hopedClassDetailInterface {
    @FormUrlEncoded
    @POST("/class/hopedClassDetail")
    fun getHopedClassDetail(
        @Field("topicId") topicId: Int,
    ): Call<hopeClassPage>
}//희망 강의 topic 받아오기


interface hopeClassTopicLikeCancelInterface {
    @FormUrlEncoded
    @POST("/class/hopedClassLikeCancel")
    fun hopeClassTopicLikeCancel(
        @Field("topicId") topicId :String,
    ): Call<resultResponse>
}//공감취소

interface hopeClassTopicLikeInterface {
    @FormUrlEncoded
    @POST("/class/hopedClassLike")
    fun hopeClassTopicLike(
        @Field("topicId") topicId :String,
        @Field("person") person : String,
        @Field("day") day  : String,
        @Field("hopedClassTime") hopedClassTime : String,
        @Field("isOn") isOn : String,
        @Field("isOff") isOff : String,
        @Field("desiredPlace") desiredPlace : String,
        @Field("type") type : String,
    ): Call<resultResponse>
}//공감


interface hopeClassForumInterface {
    @POST("/class/hopedClassTopicList")
    fun getHopeClassTopicList(): Call<List<hopeClassTopic>>
}//희망 강의 topic 받아오기

interface myHopedClassInterface {
    @POST("/class/myHopedClassGet")
    fun getmyHopedClassList(): Call<List<hopeClassTopic>>
}//my topic

interface hopeClassSearchInterface {
    @FormUrlEncoded
    @POST("/class/hopedClassSearch")
    fun getSearchedHopeClass(
        @Field("string") string:String,
        @Field("category") category:String,
    ):Call<List<hopeClassTopic>>
}//강의 취소