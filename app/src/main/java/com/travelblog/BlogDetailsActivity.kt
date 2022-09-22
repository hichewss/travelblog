package com.travelblog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.travelblog.databinding.ActivityBlogDetailsBinding

class BlogDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlogDetailsBinding

    companion object {
        private const val EXTRAS_BLOG = "EXTRAS_BLOG"

        fun start(activity: Activity, blog: Blog) {
            val intent = Intent(activity, BlogDetailsActivity::class.java)
            intent.putExtra(EXTRAS_BLOG, blog)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ratingBar.visibility = View.INVISIBLE

        loadData()

        binding.backArrow.setOnClickListener{ finish() }

        intent.extras?.getParcelable<Blog>(EXTRAS_BLOG)?.let { blog ->
            showData(blog)
        }
    }

    private fun loadData() {
        BlogHttpClient.loadBlogArticles(
            onSuccess = { list: List<Blog> ->
                runOnUiThread { showData(list[0]) }
            },
            onError = {
                runOnUiThread {showErrorSnackBar()}
            }
        )
    }

    private fun showErrorSnackBar() {
        Snackbar.make(binding.rootView, "Error loading blog articles", Snackbar.LENGTH_INDEFINITE).run {
            setActionTextColor(resources.getColor(R.color.black))
            setAction("Retry") {
                loadData()
                dismiss()
            }
        }.show()
    }

    private fun showData(blog: Blog) {
        binding.blogProgressBar.visibility = View.GONE
        binding.ratingBar.visibility = View.VISIBLE
        binding.blogTitle.text = blog.title
        binding.blogPost.text = Html.fromHtml(blog.description)
        binding.textDate.text = blog.date
        binding.blogAuthor.text = blog.author.name
        binding.ratingAmount.text = blog.rating.toString()
        binding.blogViews.text = String.format("(%d views)", blog.views)
        binding.ratingBar.rating = blog.rating

        Glide.with(this)
            .load(blog.getImageUrl())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageMain)

        Glide.with(this)
            .load(blog.author.getProfileUrl())
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.profilePicture)

    }
}