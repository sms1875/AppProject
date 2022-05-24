package com.example.application.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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

