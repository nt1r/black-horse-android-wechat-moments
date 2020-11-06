package com.example.wechat_moments.remote

import com.example.wechat_moments.viewmodels.IRepository
import com.example.wechat_moments.viewmodels.User
import io.reactivex.rxjava3.core.Maybe

class RemoteDataSource: IRepository {
    private val TAG = "Wechat"
    private val serviceBuilder = ServiceBuilder()

    override fun getCurrentUser(): Maybe<User> {
        val remoteUser = serviceBuilder.getTweetService().getUser().execute().body()

        return if (remoteUser == null) {
            Maybe.empty()
        } else {
            Maybe.just(remoteUser)
        }
    }

    override fun getAllTweets(): Maybe<List<AbstractTweet>> {
        val remoteTweets = serviceBuilder.getTweetService().getAllTweets().execute().body()

        return if (remoteTweets == null) {
            Maybe.empty()
        } else {
            Maybe.just(remoteTweets)
        }
    }
}