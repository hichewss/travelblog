package com.travelblog

import android.os.Parcelable
import android.util.Log
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

data class BlogData(val data: List<Blog>)

private val dateFormat = SimpleDateFormat("MMMM dd, yyyy")

@Parcelize
data class Blog(
    val id: String,
    val author: Author,
    val title: String,
    val date: String,
    val image: String,
    val description: String,
    val views: Int,
    val rating: Float
) : Parcelable {
    fun getImageUrl() = BlogHttpClient.BASE_URL + BlogHttpClient.PATH + image
    fun getDateMillis() = dateFormat.parse(date).time
}

@Parcelize
data class Author(val name: String, val avatar: String) : Parcelable {
    fun getProfileUrl() = BlogHttpClient.BASE_URL + BlogHttpClient.PATH + avatar

}

object BlogHttpClient {

    const val BASE_URL = "https://bitbucket.org/dmytrodanylyk/travel-blog-resources"
    const val PATH = "/raw/3eede691af3e8ff795bf6d31effb873d484877be"

    private const val BLOG_ARTICLES_URL = "$BASE_URL$PATH/blog_articles.json"

    private val executor = Executors.newFixedThreadPool(4)
    private val client = OkHttpClient()
    private val gson = Gson()

    fun loadBlogArticles(onSuccess: (List<Blog>) -> Unit, onError: () -> Unit) {
        val request = Request.Builder()
            .get()
            .url(BLOG_ARTICLES_URL)
            .build()

        executor.execute {
            kotlin.runCatching {
                val response: Response = client.newCall(request).execute()
                response.body?.string()?.let { json ->
                    gson.fromJson(json, BlogData::class.java)?.let { blogData ->
                        return@runCatching blogData.data
                    }
                }
            }.onFailure { e: Throwable ->
                Log.e("BlogHttpClient", "Error Loading Blog Articles", e)
                onError()
            }.onSuccess { value: List<Blog>? ->
                onSuccess(value ?: emptyList())
            }
        }
    }

}

