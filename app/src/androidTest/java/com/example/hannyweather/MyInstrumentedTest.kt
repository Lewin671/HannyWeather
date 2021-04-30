package com.example.hannyweather

import android.util.Log
import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyInstrumentedTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        Log.d("myTag", activityRule.scenario.state.toString())
    }

    @Test
    fun test() {
        onView(withId(R.id.searchPlaceEdit)).perform(typeText("HangZhou"), pressKey(KeyEvent.KEYCODE_ENTER))
        Thread.sleep(5000)
        Log.d("myTag", activityRule.scenario.state.toString())
    }


}