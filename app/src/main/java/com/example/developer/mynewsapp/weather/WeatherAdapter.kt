package com.example.developer.mynewsapp.weather

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.developer.mynewsapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_item.view.*
import java.util.ArrayList

class WeatherAdapter(private val weatherDataList: ArrayList<WeatherData>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): WeatherViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.weather_item, viewGroup, false)
        return WeatherViewHolder(v)
    }

    override fun getItemCount(): Int {
        return weatherDataList?.size
    }

    override fun onBindViewHolder(weatherViewHolder: WeatherViewHolder, position: Int) {
        weatherViewHolder.bind(weatherDataList!![position])
    }

    fun addWeatherList(arrayList: ArrayList<WeatherData>) {
        weatherDataList.addAll(arrayList)
    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageHolder: ImageView
        var descriptionHolder: TextView
        var temperatureHolder: TextView
        var windSpeedHolder: TextView
        var humidityHolder: TextView
        var dateHolder: TextView

        init {
            imageHolder = itemView.weather_image
            descriptionHolder = itemView.weather_description
            temperatureHolder = itemView.weather_temperature
            windSpeedHolder = itemView.weather_wind_speed
            humidityHolder = itemView.weather_humidity
            dateHolder = itemView.weather_date
        }

        internal fun bind(item: WeatherData) {
            val descriptionWeather = item.descriptionWeather
            val temperature = item.temperature
            val windSpeed = item.windSpeed
            val humidity = item.humidity
            val imageUrl = item.imageUrl
            val dateWeather = item.dateWeather

            Picasso.get().load("https://$imageUrl").into(imageHolder)
            descriptionHolder.text = descriptionWeather
            temperatureHolder.text = ("$temperature °C")
            windSpeedHolder.text = ("Cкорость ветра: $windSpeed м/с")
            humidityHolder.text = ("Влажность: $humidity %")
            dateHolder.text = ("Погода на: $dateWeather")
        }
    }
}