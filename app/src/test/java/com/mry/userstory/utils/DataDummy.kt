package com.mry.userstory.utils

import com.mry.userstory.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "https://dcblog.b-cdn.net/wp-content/uploads/2021/02/Full-form-of-URL-1-1024x824.jpg",
                "2023-02-22T22:22:22Z",
                "Name $i",
                "Description $i",
                i.toDouble() * 100,
                "id-$i",
                i.toDouble() * 100,
            )
            items.add(story)
        }
        return items
    }
}