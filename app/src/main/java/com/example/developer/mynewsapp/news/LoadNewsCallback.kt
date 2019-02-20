package com.example.developer.mynewsapp.news

interface LoadNewsCallback {

    fun onLoadNews(arrayList: ArrayList<NewsData>)
}