package com.example.developer.mynewsapp.activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.developer.mynewsapp.R
import com.example.developer.mynewsapp.TEXT_NUMBER
import com.example.developer.mynewsapp.TEXT_SITE
import kotlinx.android.synthetic.main.activity_about_program.*

class AboutProgramActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_about_program)

        text_site.setOnClickListener(this)
        text_number.setOnClickListener(this)
    }

    override fun onClick(v: View) {
       when(v.id){
           R.id.text_site -> {
               startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TEXT_SITE)))
           }
           R.id.text_number -> {
               startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(TEXT_NUMBER)))
           }
       }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
