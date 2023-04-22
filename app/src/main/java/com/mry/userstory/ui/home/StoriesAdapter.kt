package com.mry.userstory.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mry.userstory.data.response.ListStoryItem
import com.mry.userstory.databinding.StoriesItemBinding

class StoriesAdapter(private val listStories: List<ListStoryItem>): RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindStories(listStories[position])
    }

    override fun getItemCount(): Int = listStories.size

    class ViewHolder(private var binding: StoriesItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindStories(stories: ListStoryItem) {
            binding.tvTitle.text = stories.name
            binding.tvDescription.text = stories.description
            Glide.with(itemView)
                .load(stories.photoUrl)
                .into(binding.ivStory)
        }
    }

}