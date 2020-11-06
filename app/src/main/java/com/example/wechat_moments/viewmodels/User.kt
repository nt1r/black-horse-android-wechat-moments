package com.example.wechat_moments.viewmodels

import com.google.gson.annotations.SerializedName

class User(
    val name: String,
    val nick: String,
    val avatar: String,
    @SerializedName("profile-image") val profileImage: String
)