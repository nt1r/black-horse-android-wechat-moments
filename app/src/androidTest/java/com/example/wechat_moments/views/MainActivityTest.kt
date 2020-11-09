package com.example.wechat_moments.views

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.example.wechat_moments.R
import com.example.wechat_moments.ViewTestUtil.Companion.withChildViewCount
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainActivityTest {
    @get:Rule
    var activityTestRole: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun shouldShowUserNickByDefault() {
        Thread.sleep(3000L)

        val userLiveData = activityTestRole.activity.getUserLiveData()
        assertEquals(userLiveData.value!!.nick, "John Smith")

        getUserNickTextView().check(matches(withText("John Smith")))
    }

    @Test
    fun shouldShowFiveTweetsByDefault() {
        Thread.sleep(3000L)

        val tweetLiveData = activityTestRole.activity.getTweetLiveData()
        assertEquals(tweetLiveData.value!!.size, 5)
        getTweetsLinearLayout().check(matches(withChildViewCount(5, withId(R.id.tweet_constraint_layout))))
    }

    @Test
    fun shouldShowFiveMoreTweetsWhenScrollViewReachesBottom() {
        Thread.sleep(3000L)

        repeat(2) {
            getScrollView().perform(swipeUp())
        }

        Thread.sleep(500L)
        getShowingMore().check(matches(isDisplayed()))

        Thread.sleep(5000L)
        getTweetsLinearLayout().check(matches(withChildViewCount(10, withId(R.id.tweet_constraint_layout))))
    }

    @Test
    fun shouldResetTweetToFiveCountWhen() {
        Thread.sleep(3000L)

        shouldShowFiveMoreTweetsWhenScrollViewReachesBottom()

        repeat(2) {
            getScrollView().perform(swipeDown())
        }

        Thread.sleep(500L)
        getTweetsLinearLayout().check(matches(withChildViewCount(5, withId(R.id.tweet_constraint_layout))))
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