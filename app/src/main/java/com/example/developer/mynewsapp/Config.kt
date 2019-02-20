package com.example.developer.mynewsapp

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

const val JSON_WEATHER_URL = "http://api.apixu.com/v1/forecast.json?key=1547bbd657a24691b2b230942190202&q=Ufa&days=5&lang=ru"
const val JSON_NEWS_URL = "https://www.go102.ru/api2/news?type=json"
const val TEXT_SITE = "https://www.go102.ru/"
const val TEXT_NUMBER = "tel:8(987)053-0450"

@GlideModule
class CustomGlideModule: AppGlideModule()