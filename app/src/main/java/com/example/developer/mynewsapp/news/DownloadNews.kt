package com.example.developer.mynewsapp.news

import android.os.AsyncTask
import com.example.developer.mynewsapp.JSON_NEWS_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DownloadNews() : AsyncTask<Void, Void, ArrayList<NewsData>>() {

    private var newsList = ArrayList<NewsData>()
    private val okHttpClient = OkHttpClient()
    private var callback: LoadNewsCallback? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    internal constructor(callback: LoadNewsCallback) : this() {
        this.callback = callback
    }

    fun loadNews(callback: LoadNewsCallback){
        val downloadNews = DownloadNews(callback)
        downloadNews.execute()
    }

    override fun doInBackground(vararg params: Void?): ArrayList<NewsData> {

        downloadNewsFromDatabase()

        try {
            val request = Request.Builder().url(JSON_NEWS_URL).build()
            val response = okHttpClient.newCall(request).execute()
            val responseString = response.body()?.string()

            val jsonObject = JSONObject(responseString)
            val jsonObjectResponse = jsonObject.getJSONObject("response")
            val jsonArrayNews = jsonObjectResponse.getJSONArray("news")

            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm a")
            val dateObject = java.util.Date()

            for (i in 0 until jsonArrayNews.length()) {
                val jsonObject = jsonArrayNews.getJSONObject(i)

                val name = jsonObject.getString("name")
                val text = jsonObject.getString("txt")
                val dateUnix = jsonObject.getString("date_show_unix")
                val dateConvert = java.lang.Long.valueOf(dateUnix) * 1000
                dateObject.time = dateConvert
                val date = simpleDateFormat.format(dateObject)

                val objectNewsIcon = jsonObject.getJSONObject("newsIcon")
                val imageUrl = objectNewsIcon.getString("imgfull")
                val important = 1 == jsonObject.getInt("top_day")

                newsList.add(NewsData(name, text, date, imageUrl, important))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        newsList.sortWith(Comparator { abc1, abc2 -> abc2.important.compareTo(abc1.important) })

        return newsList
    }

     private fun downloadNewsFromDatabase(){
             try {
                 database.child("users").child(auth.currentUser!!.uid)
                     .addValueEventListener(object : ValueEventListener {
                         override fun onDataChange(data: DataSnapshot) {
                             if (data.children != null) {
                                 val list: List<Any?> = data.children.map { it.value }
                                 val title = list[4].toString()
                                 val text = list[3].toString()
                                 val date = list[0].toString()
                                 val image = list[1].toString()
                                 val important = false

                                 newsList.add(
                                     NewsData(
                                         title,
                                         text,
                                         date,
                                         image,
                                         important
                                     )
                                 )
                             }
                         }
                         override fun onCancelled(p0: DatabaseError?) {}
                     })
             } catch (e: Exception) {
                 e.printStackTrace()
             }
    }

    override fun onPostExecute(list: ArrayList<NewsData>) {
        super.onPostExecute(list)
        callback?.onLoadNews(list)
    }
}
