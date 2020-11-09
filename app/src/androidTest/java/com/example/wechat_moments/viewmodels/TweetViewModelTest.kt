package com.example.wechat_moments.viewmodels

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.example.wechat_moments.R
import com.example.wechat_moments.remote.AbstractTweet
import com.example.wechat_moments.remote.RemoteDataSource
import com.example.wechat_moments.views.MainActivity
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Maybe
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TweetViewModelTest {
    @get:Rule
    var activityTestRole: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    // private val mockedDataSource: RemoteDataSource = mockk()
    private val currentUser = User(
        "leqi",
        /*"John Smith",*/
        "leqi",
        "https://s3.amazonaws.com/uifaces/faces/twitter/mahmoudmetwally/128.jpg",
        "http://lorempixel.com/480/280/technics/6/"
    )

    private val sampleTweets = mutableListOf<AbstractTweet>(
        Tweet(
            "沙发！",
            mutableListOf(
                "http://lorempixel.com/400/200/abstract/3",
                "http://lorempixel.com/400/200/life/4",
                "http://lorempixel.com/400/480/food/2"
            ),
            currentUser,
            mutableListOf(
                Comment(
                    "Good.",
                    currentUser
                ),
                Comment(
                    "Like it too",
                    currentUser
                )
            )
        )
    )

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun shouldShowUserNickByDefault() {
        val mockedDataSource: RemoteDataSource = mockk(relaxed = true)
        every { mockedDataSource.getCurrentUser() } returns Maybe.just(currentUser)
        activityTestRole.activity.tweetViewModel.setRepository(mockedDataSource)

        activityTestRole.activity.tweetViewModel.requestCurrentUser()
        Thread.sleep(500L)

        verify {
            mockedDataSource.getCurrentUser()
        }
        confirmVerified(mockedDataSource)
        // getUserNickTextView().check(matches(withText("leqi")))
        // assertEquals("leqi", activityTestRole.activity.getUserLiveData().value!!.nick)
    }

    @Test
    fun shouldFetchAllTweets() {
        val mockedDataSource: RemoteDataSource = mockk()
        every { mockedDataSource.getAllTweets() } returns Maybe.just(sampleTweets)
        activityTestRole.activity.tweetViewModel.setRepository(mockedDataSource)

        activityTestRole.activity.tweetViewModel.requestAllTweets()
        Thread.sleep(500L)

        verify {
            mockedDataSource.getAllTweets()
        }
        confirmVerified(mockedDataSource)
        assertEquals("沙发！", activityTestRole.activity.getTweetLiveData().value!![0].content)
    }

    private fun getUserNickTextView(): ViewInteraction {
        return onView(withId(R.id.user_nick_name))
    }

    private fun getTweetsLinearLayout(): ViewInteraction {
        return onView(withId(R.id.tweet_list_linear_layout))
    }

    private fun getScrollView(): ViewInteraction {
        return onView(withId(R.id.nested_scroll_view))
    }

    private fun getShowingMore(): ViewInteraction {
        return onView(withId(R.id.showing_more_constraint_layout))
    }
}