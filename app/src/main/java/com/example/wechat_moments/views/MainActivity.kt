package com.example.wechat_moments.views

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.example.wechat_moments.R
import com.example.wechat_moments.databinding.ActivityMainBinding
import com.example.wechat_moments.databinding.SingleCommentBinding
import com.example.wechat_moments.databinding.SingleTweetBinding
import com.example.wechat_moments.viewmodels.Tweet
import com.example.wechat_moments.viewmodels.TweetViewModel
import com.example.wechat_moments.viewmodels.User
import com.example.wechat_moments.views.adapters.ImageAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "Wechat"
    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingView: View
    val tweetViewModel: TweetViewModel by viewModels()

    private var lastShownTweetCount = 0

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
        if (loadingView.visibility == VISIBLE) {
            binding.tweetListLinearLayout.removeViewAt(binding.tweetListLinearLayout.childCount - 1)
            loadingView.visibility = GONE
        }

        if (tweets.size >= lastShownTweetCount) {
            tweets.subList(lastShownTweetCount, tweets.size).forEach { tweet ->
                // Log.d(TAG, GsonBuilder().setPrettyPrinting().create().toJson(tweet))
                val tweetView = layoutInflater.inflate(R.layout.single_tweet, null)
                val tweetBinding = SingleTweetBinding.bind(tweetView)

                Glide.with(tweetView.context)
                    .load(tweet.sender.avatar)
                    .into(tweetBinding.senderAvatar)

                adaptTextViews(tweetBinding, tweet)
                adaptTweetsImages(tweet, tweetBinding, tweetView)
                adaptTweetsComments(tweet, tweetBinding)

                binding.tweetListLinearLayout.addView(tweetView)
            }
            lastShownTweetCount = tweets.size
        }
    }

    private fun adaptTextViews(
        tweetBinding: SingleTweetBinding,
        tweet: Tweet
    ) {
        tweetBinding.senderNickName.text = tweet.sender.nick
        if (tweet.content == "") {
            tweetBinding.tweetContent.visibility = GONE
        } else {
            tweetBinding.tweetContent.text = tweet.content
        }
    }

    private fun adaptTweetsComments(
        tweet: Tweet,
        tweetBinding: SingleTweetBinding
    ) {
        if (tweet.comments.isNotEmpty()) {
            tweet.comments.forEach { comment ->
                val commentView = layoutInflater.inflate(R.layout.single_comment, null)
                val commentBinding = SingleCommentBinding.bind(commentView)
                commentBinding.commentNickName.text = comment.sender.nick
                commentBinding.commentContent.text = comment.content
                tweetBinding.commentsLinearLayout.addView(commentView)
            }
        } else {
            tweetBinding.commentsLinearLayout.visibility = GONE
        }
    }

    private fun adaptTweetsImages(
        tweet: Tweet,
        tweetBinding: SingleTweetBinding,
        tweetView: View
    ) {
        when (tweet.images.size) {
            1 -> {
                val imageView = ImageView(tweetView.context)
                Glide.with(this)
                    .load(tweet.images[0])
                    .into(imageView)
                tweetBinding.tweetFlexboxLayout.addView(imageView)
                tweetBinding.tweetFlexboxLayout.visibility = VISIBLE
            }
            2, 3 -> {
                val imageAdapterFirst = ImageAdapter(tweetView.context, tweet.images, layoutInflater)
                tweetBinding.gridViewFirstLine.adapter = imageAdapterFirst
                tweetBinding.gridViewFirstLine.visibility = VISIBLE
            }
            4, 5, 6 -> {
                val imageAdapterFirst = ImageAdapter(tweetView.context, tweet.images.subList(0, 3), layoutInflater)
                val imageAdapterSecond = ImageAdapter(tweetView.context, tweet.images.subList(3, tweet.images.size), layoutInflater)
                tweetBinding.gridViewFirstLine.adapter = imageAdapterFirst
                tweetBinding.gridViewSecondLine.adapter = imageAdapterSecond
                tweetBinding.gridViewFirstLine.visibility = VISIBLE
                tweetBinding.gridViewSecondLine.visibility = VISIBLE
            }
            else -> {
            }
        }
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

        loadingView = layoutInflater.inflate(R.layout.showing_more, null)
        loadingView.visibility = GONE

        binding.swipeRefreshLayout.setOnRefreshListener {
            // Log.d(TAG, "Top refresh.")
            binding.swipeRefreshLayout.isRefreshing = false
            lastShownTweetCount = 0
            binding.tweetListLinearLayout.removeAllViews()
            tweetViewModel.resetTweets()
        }

        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            if (isScrollViewReachBottom(binding.nestedScrollView) && loadingView.visibility == GONE) {
                loadingView.visibility = VISIBLE
                binding.tweetListLinearLayout.addView(loadingView)
                GlobalScope.launch(Dispatchers.IO) {
                    // mimic network fetch
                    delay(2000L)
                    tweetViewModel.updateMoreTweets()
                }
            }
        }
    }

    private fun isScrollViewReachBottom(nestedScrollView: NestedScrollView): Boolean {
        return nestedScrollView.getChildAt(0).bottom <= nestedScrollView.height + nestedScrollView.scrollY
    }

    fun getTweetLiveData(): LiveData<List<Tweet>> {
        return tweetViewModel.tweetsLiveData
    }

    fun getUserLiveData(): LiveData<User> {
        return tweetViewModel.userLiveData
    }
}