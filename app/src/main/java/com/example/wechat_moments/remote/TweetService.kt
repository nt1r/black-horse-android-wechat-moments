package com.example.wechat_moments.remote

import com.example.wechat_moments.viewmodels.User
import retrofit2.Call
import retrofit2.http.GET

interface TweetService {
    @GET("tweets.json")
    fun getAllTweets() : Call<List<AbstractTweet>>

    @GET("profile.json")
    fun getUser(): Call<User>
}