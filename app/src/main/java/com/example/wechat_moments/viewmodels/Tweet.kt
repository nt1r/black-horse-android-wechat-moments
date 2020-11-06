package com.example.wechat_moments.viewmodels

import com.example.wechat_moments.remote.AbstractTweet

class Tweet(
    val content: String,
    val images: List<String>,
    val sender: User,
    val comments: List<Comment>
) : AbstractTweet()