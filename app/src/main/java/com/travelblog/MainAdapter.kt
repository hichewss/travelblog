package com.travelblog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.travelblog.databinding.ItemMainBinding
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MainAdapter(private val onItemClickListener: (Blog) -> Unit) :  ListAdapter<Blog,
        MainViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainBinding.inflate(inflater, parent, false)

        return MainViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    fun sortByTitle() {
        submitList(currentList.sortedBy { blog -> blog.title })
    }

    fun sortByDate() {
        submitList(currentList.sortedBy { blog -> blog.getDateMillis() })
    }

    private var originalList: List<Blog> = ArrayList()

    fun setData(list: List<Blog>) {
        originalList = list
        super.submitList(list)
    }

    fun filter(query: String) {
        submitList(originalList
            .filter { blog -> blog.title.contains(query, ignoreCase = true) })
    }

}

class MainViewHolder(private val binding: ItemMainBinding, private val onItemClickListener: (Blog) -> Unit)
    : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(blog: Blog) {
        binding.root.setOnClickListener{ onItemClickListener(blog) }
        binding.blogTitle.text = blog.title
        binding.blogDate.text = blog.date

        Glide.with(itemView)
            .load(blog.author.getProfileUrl())
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.profile)
    }
}

private val DIFF_CALLBACK: DiffUtil.ItemCallback<Blog> = object : DiffUtil.ItemCallback<Blog>() {
    override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return oldItem == newItem
    }
}