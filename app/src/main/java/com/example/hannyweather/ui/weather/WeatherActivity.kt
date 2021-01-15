package com.example.hannyweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.hannyweather.R
import com.example.hannyweather.R.id
import com.example.hannyweather.R.layout
import com.example.hannyweather.databinding.ActivityWeatherBinding
import com.example.hannyweather.logic.model.Weather
import com.example.hannyweather.logic.model.getSky
import com.example.hannyweather.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    public lateinit var binding: ActivityWeatherBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }

        //setContentView(R.layout.activity_weather)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        viewModel.weatherLiveData.observe(this) {
            val weather = it.getOrNull()

            if (weather == null) {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            } else {
                showWeatherInfo(weather)
            }

            binding.swipeRefresh.isRefreshing = false
        }
        //viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        refreshWeather()
        // 下拉刷新响应
        binding.swipeRefresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary, theme))
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
            Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show()
        }

        // 导航栏按钮
        val navBtn = binding.now.navBtn
        val drawLayout = binding.drawerLayout

        navBtn.setOnClickListener {
            drawLayout.openDrawer(GravityCompat.START)
        }

        drawLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.now.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        // 填充now.xml的数据
        // 当前温度
        val nowBinding = binding.now
        val currentTempText = "${realtime.temperature} °C"
        nowBinding.currentTemp.text = currentTempText
        nowBinding.currentSky.text = getSky(realtime.skycon).info
        val currentPm25Text = "空气指数${realtime.airQuality.aqi.chn.toInt()}"
        nowBinding.currentAQI.text = currentPm25Text
        nowBinding.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        // 填充forecast.xml布局
        val forecastBinding = binding.forecast
        forecastBinding.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(
                    layout.forecast_item,
                    forecastBinding.forecastLayout, false
            )
            val dateInfo = view.findViewById<TextView>(id.dataInfo)
            val skyIcon = view.findViewById<ImageView>(id.skyIcon)
            val skyInfo = view.findViewById<TextView>(id.skyInfo)
            val temperatureInfo = view.findViewById<TextView>(id.temperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()}~${temperature.max.toInt()}°C"
            temperatureInfo.text = tempText
            forecastBinding.forecastLayout.addView(view)
        }

        // 填充life_index.xml
        val lifeIndexBinding = binding.lifeIndex
        val lifeIndex = daily.lifeIndex
        lifeIndexBinding.coldRiskText.text = lifeIndex.coldRisk[0].desc
        lifeIndexBinding.dressingText.text = lifeIndex.dressing[0].desc
        lifeIndexBinding.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        lifeIndexBinding.carWashingText.text = lifeIndex.carWashing[0].desc

        binding.weatherLayout.visibility = View.VISIBLE
    }
}