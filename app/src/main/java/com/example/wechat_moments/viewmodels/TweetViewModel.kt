package com.example.wechat_moments.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wechat_moments.remote.RemoteDataSource
import com.example.wechat_moments.remote.TweetError
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.min

class TweetViewModel : ViewModel() {
    private val TAG = "Wechat"
    private val _userLiveData: MutableLiveData<User> by lazy {
        MutableLiveData()
    }
    val userLiveData: LiveData<User>
        get() = _userLiveData

    private val _tweetsLiveData: MutableLiveData<List<Tweet>> by lazy {
        MutableLiveData()
    }
    val tweetsLiveData: LiveData<List<Tweet>>
        get() = _tweetsLiveData

    var allTweets: List<Tweet> = listOf()

    private var repository: IRepository? = null

    private val defaultUser = User(
        "User",
        "Nick",
        "drawable/sample_bg.jpeg",
        "drawable/sample_bg.jpeg"
    )
    private val PER_REFRESH_COUNT = 5

    fun setRepository(repository: IRepository) {
        this.repository = repository
    }

    fun requestCurrentUser() {
        if (repository == null) {
            repository = RemoteDataSource()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository!!.getCurrentUser()
                .subscribeBy(
                    onSuccess = { user ->
                        // Log.d(TAG, "requestCurrentUser: ${user.profileImage}, ${user.name}")
                        _userLiveData.postValue(user)
                    },
                    onComplete = {
                        // user not found
                        _userLiveData.postValue(defaultUser)
                        Log.d(TAG, "user not found")
                    }
                )
        }
    }

    fun requestAllTweets() {
        if (repository == null) {
            repository = RemoteDataSource()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository!!.getAllTweets()
                .subscribeBy(
                    onSuccess = { tweets ->
                        allTweets = tweets.filterIsInstance<Tweet>()
                        var filteredTweets = allTweets
                        if (allTweets.size > PER_REFRESH_COUNT) {
                            filteredTweets = allTweets.subList(0, PER_REFRESH_COUNT)
                        }
                        _tweetsLiveData.postValue(filteredTweets)
                    },
                    onComplete = {
                        // user not found
                        Log.d(TAG, "tweets not found")
                    }
                )
        }
    }

    fun updateMoreTweets() {
        val currentTweetCount = _tweetsLiveData.value!!.size
        val updateToIndex = min(currentTweetCount + PER_REFRESH_COUNT, allTweets.size)
        _tweetsLiveData.postValue(allTweets.subList(0, updateToIndex))
    }

    fun resetTweets() {
        val updateToIndex = min(PER_REFRESH_COUNT, allTweets.size)
        _tweetsLiveData.postValue(allTweets.subList(0, updateToIndex))
    }
}