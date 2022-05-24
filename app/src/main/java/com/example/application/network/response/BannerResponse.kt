package com.example.application.network.response

import com.google.gson.annotations.SerializedName

data class bannerList(
    @SerializedName("bannerList") val bannerList: List<String>,
)