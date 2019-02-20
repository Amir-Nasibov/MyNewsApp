package com.example.developer.mynewsapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.developer.mynewsapp.*
import com.example.developer.mynewsapp.weather.WeatherAdapter
import com.example.developer.mynewsapp.weather.WeatherData
import com.example.developer.mynewsapp.weather.WeatherPresenter
import kotlinx.android.synthetic.main.activity_weather.*


class WeatherActivity : AppCompatActivity() {

    private var weatherList = ArrayList<WeatherData>()
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherPresenter: WeatherPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_weather)

        recycler_view_weather.layoutManager = LinearLayoutManager(this)
        weatherAdapter = WeatherAdapter(weatherList)
        recycler_view_weather.adapter = weatherAdapter

        weatherPresenter = WeatherPresenter(this)
        weatherPresenter.startDownloadingWeather()
    }

    fun showWeather(list: ArrayList<WeatherData>) {
        weatherAdapter.addWeatherList(list)
        weatherAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
