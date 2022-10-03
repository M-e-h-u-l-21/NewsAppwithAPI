package com.example.newsappwithapi

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var madapter:NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        madapter= NewsListAdapter(this)

        recyclerView.adapter=madapter//attaching adapter with recyclerview adapter
    }

    private fun fetchData(){
        val url="https://newsapi.org/v2/top-headlines?country=in&apiKey=c521262b956d4b57ae5ee09d854135c1"
        val jsonObjectRequest:JsonObjectRequest=object:JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
            val newsJsonArray=it.getJSONArray("articles")
                val newsArray=ArrayList<News>()
                for (i in 0 until newsJsonArray.length()){
                    val newsJSONObject=newsJsonArray.getJSONObject(i)
                    val news=News(
                        newsJSONObject.getString("title"),
                        newsJSONObject.getString("author"),
                        newsJSONObject.getString("url"),
                        newsJSONObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                madapter.updateNews(newsArray)
            },
            {

            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {

        val builder=CustomTabsIntent.Builder()
        val customTabsIntent=builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}