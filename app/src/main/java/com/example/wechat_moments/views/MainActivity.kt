package com.example.wechat_moments.views

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.wechat_moments.R
import com.example.wechat_moments.databinding.ActivityMainBinding
import com.example.wechat_moments.viewmodels.Tweet
import com.example.wechat_moments.viewmodels.TweetViewModel
import com.example.wechat_moments.viewmodels.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {
    private val TAG = "Wechat"
    private lateinit var binding: ActivityMainBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val tweetViewModel: TweetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.bind(layoutInflater.inflate(R.layout.activity_main, null))
        setContentView(binding.root)

        initComponents()
        initViewModels()

        tweetViewModel.requestCurrentUser()
        tweetViewModel.requestAllTweets()
    }

    private fun initViewModels() {
        tweetViewModel.userLiveData.observe(this, { user ->
            adaptUserViews(user)
        })
        tweetViewModel.tweetsLiveData.observe(this, { tweets ->
            adaptTweets(tweets)
        })
    }

    private fun adaptTweets(tweets: List<Tweet>) {
        Log.d(TAG, GsonBuilder().setPrettyPrinting().create().toJson(tweets))
    }

    private fun adaptUserViews(user: User?) {
        if (user != null) {
            binding.userNickName.text = user.nick
            if (user.profileImage.isNotEmpty()) {
                Glide.with(this)
                    .load(user.profileImage)
                    .into(binding.backgroundImageView)
            }
            if (user.avatar.isNotEmpty()) {
                Glide.with(this)
                    .load(user.avatar)
                    .into(binding.userAvatar)
            }
        }
    }

    private fun initComponents() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }
    }
}