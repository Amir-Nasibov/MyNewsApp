package com.example.developer.mynewsapp.news

import com.example.developer.mynewsapp.activities.MainActivity
import java.util.ArrayList


class NewsPresenter(private val view: MainActivity) {
    private val downloadNews = DownloadNews()

    fun startDownloadingNews() {
        downloadNews.loadNews(object : LoadNewsCallback {
            override fun onLoadNews(arrayList: ArrayList<NewsData>) {
                view.showNews(arrayList)
            }
        })
    }
}



