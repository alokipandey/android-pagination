package com.example.mytube.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mytube.repository.Video

class RVDiffCallback(val oldPosts:ArrayList<Video>,val newPosts: List<Video>):DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPosts[oldItemPosition] == newPosts[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldPosts.size
    }

    override fun getNewListSize(): Int {
        return newPosts.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldPosts[oldItemPosition]
        val new = newPosts[newItemPosition]
        return old.event_name == new.event_name && old.event_date == new.event_date
    }

}