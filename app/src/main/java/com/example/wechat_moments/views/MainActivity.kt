package com.example.wechat_moments.views

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.wechat_moments.R
import com.example.wechat_moments.databinding.ActivityMainBinding
import com.example.wechat_moments.databinding.SingleCommentBinding
import com.example.wechat_moments.databinding.SingleTweetBinding
import com.example.wechat_moments.viewmodels.Tweet
import com.example.wechat_moments.viewmodels.TweetViewModel
import com.example.wechat_moments.viewmodels.User

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
        tweets.forEach { tweet ->
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
        if (tweet.images.isEmpty()) {
            tweetBinding.tweetFlexboxLayout.visibility = GONE
        }

        tweet.images.forEach { imageUrl ->
            val imageView = ImageView(tweetView.context)
            Glide.with(imageView)
                .load(imageUrl)
                .into(imageView)
            tweetBinding.tweetFlexboxLayout.addView(imageView)
        }
        /*val imageAdapter = ImageAdapter(tweetView.context, tweet.images)
        tweetBinding.tweetGridView.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()*/
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