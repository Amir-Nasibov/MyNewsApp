package com.example.developer.mynewsapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.developer.mynewsapp.news.NewsAdapter
import com.example.developer.mynewsapp.news.NewsData
import com.example.developer.mynewsapp.news.NewsPresenter
import com.example.developer.mynewsapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    NewsAdapter.OnItemClickListener {

    private var newsList = ArrayList<NewsData>()
    private lateinit var auth: FirebaseAuth
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsPresenter: NewsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        refresh.isRefreshing = true
        recycler_view.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter(newsList)
        recycler_view.adapter = newsAdapter
        newsAdapter.setOnItemClickListener(this)

        newsPresenter = NewsPresenter(this)
        newsPresenter.startDownloadingNews()

        initSwipeRefreshLayout()
        auth = FirebaseAuth.getInstance()

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        val item =  nav_view.menu.getItem(2)
        if (auth.currentUser != null){
            item.title = "Выход"
            item.setIcon(R.drawable.ic_exit)
        }
    }

    fun showNews(list: ArrayList<NewsData>) {
        newsAdapter.refreshList(list)
        newsAdapter.notifyDataSetChanged()
        refresh.isRefreshing = false
    }

    private fun initSwipeRefreshLayout() {
        refresh.setOnRefreshListener { newsPresenter.startDownloadingNews() }
    }

    private fun showAlertSignIn() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Это действие доступно только авторизованым пользователям. Хотите авторизоваться сейчас?")
        builder.setPositiveButton("Да") {
                dialog, which ->
            startActivity(Intent(this, AuthorizationActivity::class.java))
        }
        builder.setNegativeButton("Нет") {
                dialog, which ->
            dialog.cancel()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertExit() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Вы действительно хотите выйти?")
        builder.setPositiveButton("Да") {
                dialog, which ->
            auth.signOut()
            val item =  nav_view.menu.getItem(2)
            item.title = "Авторизация"
            item.setIcon(R.drawable.ic_authorization)
        }
        builder.setNegativeButton("Нет") {
                dialog, which ->
            dialog.cancel()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_add_news -> {
                if (auth.currentUser == null) {
                    showAlertSignIn()
                } else {
                    startActivity(Intent(this, AddNewsActivity::class.java))
                }
            }
            R.id.nav_weather -> {
                startActivity(Intent(this, WeatherActivity::class.java))
            }
            R.id.nav_authorization -> {
                if (auth.currentUser != null){
                   showAlertExit()
                } else {
                    startActivity(Intent(this, AuthorizationActivity::class.java))
                }
            }
            R.id.nav_about_program -> {
                startActivity(Intent(this, AboutProgramActivity::class.java))
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onItemClick(newsData: NewsData) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("NewsData", newsData)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
