package com.example.application.network.service

import com.example.application.network.response.placeDataList
import com.example.application.network.response.resultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap

interface getReservationPlaceInterface {
    @POST("/space/getMyPlace")
    fun getReservationPlaceList(): Call<placeDataList>
}//장소

interface placeRegisterInterface {
    @Multipart
    @POST("/space/register")
    fun placeRegister(
        @Part placeImage: ArrayList<MultipartBody.Part>,
        @PartMap data: HashMap<String, RequestBody>
    ): Call<resultResponse>
}//장소 등록 서비스

interface getplaceInterface {
    @POST("/space/get")
    fun getplaceList(): Call<placeDataList>
}//장소

interface reservationPlaceInterface {
    @FormUrlEncoded
    @POST("/space/reservation")
    fun reservationPlace(@Field("placeId") placeId: String?
    ): Call<resultResponse>
}//장소등록

interface cancelRegisterPlaceInterface {
    @FormUrlEncoded
    @POST("/space/cancelRegister")
    fun reservationPlace(@Field("placeId") placeId: String?
    ): Call<resultResponse>
}//장소등록

interface getMyRegisterPlaceInterface {
    @POST("/space/getMyRegisterPlace")
    fun getMyRegisterPlaceList(): Call<placeDataList>
}//장소
