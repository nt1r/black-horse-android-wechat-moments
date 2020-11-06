package com.example.wechat_moments.remote

import com.example.wechat_moments.viewmodels.Tweet
import com.example.wechat_moments.viewmodels.User
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder {
    private val tweetServiceBaseUrl = "https://twc-android-bootcamp.github.io/fake-data/data/weixin/"
    private var instance: Retrofit? = null
    private var tweetService: TweetService? = null

    private fun buildRetrofit(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(User::class.java, UserDeserializer())
                        .registerTypeAdapter(AbstractTweet::class.java, TweetDeserializer())
                        .create())
                )
                .baseUrl(tweetServiceBaseUrl)
                .build()
        }
        return instance!!
    }

    fun getTweetService(): TweetService {
        if (tweetService == null) {
            tweetService = buildRetrofit()
                .create(TweetService::class.java)
        }
        return tweetService!!
    }
}