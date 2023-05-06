package com.mry.userstory.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mry.userstory.data.response.ListStoryItem
import com.mry.userstory.databinding.StoriesItemBinding

class StoriesAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<ListStoryItem, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {
    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        if (data != null) {
            holder.bindStories(data)
            holder.itemView.setOnClickListener {
                listener.onItemClick(data.id.toString())
            }
        }
    }

    class ViewHolder(private val binding: StoriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindStories(stories: ListStoryItem) {
            Glide.with(itemView)
                .load(stories.photoUrl)
                .into(binding.ivStory)
            binding.apply {
                tvTitle.text = stories.name
                tvDescription.text = stories.description
                ivStory.contentDescription = stories.description
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}