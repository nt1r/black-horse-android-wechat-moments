package com.example.wechat_moments.viewmodels

import com.example.wechat_moments.remote.AbstractTweet
import io.reactivex.rxjava3.core.Maybe

interface IRepository {
    fun getCurrentUser(): Maybe<User>
    fun getAllTweets(): Maybe<List<AbstractTweet>>
}