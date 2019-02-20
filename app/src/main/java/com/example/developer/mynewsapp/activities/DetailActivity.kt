package com.example.developer.mynewsapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.developer.mynewsapp.news.NewsData
import com.example.developer.mynewsapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var newsData: NewsData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_detail)

        newsData = intent.getParcelableExtra("NewsData")
        val name = newsData.name
        val text = newsData.text
        val date = newsData.date
        val imageUrl = newsData.imageUrl

        Picasso.get().load(imageUrl).into(image_view_detail)
        text_view_name_detail.text = name
        text_view_text_detail.text = text
        text_view_date_detail.text = date
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_item) {
            val intent = intent
            val intentAction = Intent(intent.action)
            intentAction.type = "text/plain"
            val stringAll = newsData.imageUrl + " " + newsData.name
            intentAction.putExtra(android.content.Intent.EXTRA_TEXT, stringAll)
            intentAction.putExtra(android.content.Intent.EXTRA_SUBJECT, "")
            startActivity(Intent.createChooser(intentAction, "Поделиться новостью"))
        } else if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
