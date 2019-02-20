package com.example.developer.mynewsapp.weather

import com.example.developer.mynewsapp.activities.WeatherActivity
import java.util.ArrayList

class WeatherPresenter(private val view: WeatherActivity) {
    private val downloadWeather = DownloadWeather()

    fun startDownloadingWeather() {
        downloadWeather.loadWeather(object : LoadWeatherCallback {
            override fun onLoadWeather(arrayList: ArrayList<WeatherData>) {
                view.showWeather(arrayList)
            }
        })
    }
}