package com.example.developer.mynewsapp.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView
import com.example.developer.mynewsapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_item.view.*
import java.util.ArrayList

class NewsAdapter(private val exampleList: ArrayList<NewsData>) : RecyclerView.Adapter<NewsAdapter.ExampleViewHolder>() {

    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(newsData: NewsData)
    }

    fun refreshList(arrayList: ArrayList<NewsData>) {
        exampleList?.clear()
        exampleList.addAll(arrayList)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ExampleViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.news_item, viewGroup, false)
        return ExampleViewHolder(v)
    }

    override fun getItemCount(): Int {
        return exampleList?.size
    }

    override fun onBindViewHolder(exampleViewHolder: ExampleViewHolder, position: Int) {
        exampleViewHolder.bind(exampleList!![position])
    }

    inner class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageViewHolder: ImageView
        var nameHolder: TextView
        var textHolder: TextView
        var dateHolder: TextView
        var importantHolder: TextView

        init {
            nameHolder = itemView.text_view_name
            textHolder = itemView.text_view_text
            dateHolder = itemView.date_unix
            imageViewHolder = itemView.image_view
            importantHolder = itemView.text_view_important
        }

        internal fun bind(item: NewsData) {
            itemView.tag = item
            val name = item.name
            val text = item.text
            val date = item.date
            val imageUrl = item.imageUrl
            val important = item.important

            nameHolder.text = name
            textHolder.text = text
            dateHolder.text = date
            Picasso.get().load(imageUrl).into(imageViewHolder)

            if (important) {
                importantHolder.visibility = View.VISIBLE
            } else {
                importantHolder.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener { v ->
                val item1 = v.tag as NewsData
                listener?.onItemClick(item1)
            }
        }
    }
}

