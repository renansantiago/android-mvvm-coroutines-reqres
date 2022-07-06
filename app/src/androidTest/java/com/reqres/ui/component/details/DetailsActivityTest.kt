package com.reqres.ui.component.details

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.reqres.R
import com.reqres.USER_ITEM_KEY
import com.reqres.TestUtil.initData
import com.reqres.TestUtil.users
import com.reqres.utils.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DetailsActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityTestRule = ActivityTestRule(DetailsActivity::class.java, true, false)
    private var mIdlingResource: IdlingResource? = null


    @Before
    fun setup() {
        initData()
        val intent: Intent = Intent().apply {
            putExtra(USER_ITEM_KEY, users.usersList[0])
        }
        mActivityTestRule.launchActivity(intent)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testUserDetails() {
        //Assert Title
        onView(withId(R.id.tv_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_name)).check(matches(withText(users.usersList[0].fullName)))
        //Assert User Email
        onView(withId(R.id.tv_email)).check(matches(withText(users.usersList[0].email)))
        //Assert Add to Favorite
        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).check(matches(isClickable()))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister()
        }
    }
}
