package com.example.developer.mynewsapp.weather

import android.os.AsyncTask
import com.example.developer.mynewsapp.JSON_WEATHER_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class DownloadWeather() : AsyncTask<Void, Void, ArrayList<WeatherData>>() {

    private var weatherList = ArrayList<WeatherData>()
    private val okHttpClient = OkHttpClient()
    private var callback: LoadWeatherCallback? = null

    internal constructor(callback: LoadWeatherCallback) : this() {
        this.callback = callback
    }

    fun loadWeather(callback: LoadWeatherCallback){
        val downloadWeather = DownloadWeather(callback)
        downloadWeather.execute()
    }

    override fun doInBackground(vararg params: Void?): ArrayList<WeatherData>{

        try {
            val request = Request.Builder().url(JSON_WEATHER_URL).build()
            val response = okHttpClient.newCall(request).execute()
            val responseString = response.body()?.string()

            val jsonObject = JSONObject(responseString)
            val jsonObjectForecast = jsonObject.getJSONObject("forecast")
            val jsonArrayForecastday = jsonObjectForecast.getJSONArray("forecastday")

            for (i in 0 until jsonArrayForecastday.length()) {
                val jsonObject = jsonArrayForecastday.getJSONObject(i)

                val dateWeather = jsonObject.getString("date")

                val jsonObjectDay = jsonObject.getJSONObject("day")
                val temperature = jsonObjectDay.getString("avgtemp_c")
                val windSpeed = jsonObjectDay.getString("maxwind_kph")
                val humidity = jsonObjectDay.getString("avghumidity")

                val jsonObjectCondition = jsonObjectDay.getJSONObject("condition")
                val descriptionWeather = jsonObjectCondition.getString("text")
                val imageUrl = jsonObjectCondition.getString("icon")

                weatherList.add(
                    WeatherData(
                        descriptionWeather,
                        temperature,
                        windSpeed,
                        humidity,
                        imageUrl,
                        dateWeather
                    )
                )
            }
        } catch (e:JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return weatherList
    }

    override fun onPostExecute(list: ArrayList<WeatherData>) {
        super.onPostExecute(list)
        callback?.onLoadWeather(list)
    }
}
