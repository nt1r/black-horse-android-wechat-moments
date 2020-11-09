package com.example.wechat_moments.remote

import com.example.wechat_moments.viewmodels.Tweet
import org.junit.After
import org.junit.Before
import org.junit.Test

class RemoteDataSourceUnitTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun shouldFetchUserData() {
        val remoteDataSource = RemoteDataSource()
        remoteDataSource.getCurrentUser().test()
            .assertValueCount(1)
            .assertValue { user ->
                user.nick == "John Smith"
            }
            .assertComplete()
    }

    @Test
    fun shouldFetchAllTweetsData() {
        val remoteDataSource = RemoteDataSource()
        remoteDataSource.getAllTweets().test()
            .assertValueCount(1)
            .assertValue { tweets ->
                tweets.size > 14
                        && tweets.filterIsInstance(Tweet::class.java).size == 17
            }
            .assertComplete()
    }
}