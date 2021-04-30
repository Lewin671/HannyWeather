package com.example.hannyweather.logic.network

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hannyweather.Logger
import com.example.hannyweather.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HannyWeatherNetworkTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Logger.d("HannyWeatherNetworkTest started")
        Logger.d(activityRule.scenario.state.toString())
    }

    @Test
    fun testSearchPlace() {
        val queriedPlace = "杭州"
        activityRule.scenario.onActivity {
            runBlocking {
                val places = HannyWeatherNetwork.searchPlace(queriedPlace)
                assertTrue(places.toString().contains(queriedPlace))
                //Logger.d(places.toString())
            }
        }

    }

    @After
    fun tearDown() {
        Logger.d("HannyWeatherNetworkTest ended")
    }
}