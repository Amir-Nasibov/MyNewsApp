package com.example.developer.mynewsapp.weather

interface LoadWeatherCallback {
    fun onLoadWeather(arrayList: ArrayList<WeatherData>)
}