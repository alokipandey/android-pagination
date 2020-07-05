package com.example.mytube.adapter

enum class SpinnerItems(val value: String) {
    NEW("Newest"),
    OLD("Oldest"),
    SHARED_ASC("Least shared"),
    SHARED_DESC("Most Shared"),
    LIKE_ASC("Like--min to max"),
    LIKE_DESC("Like--max to min"),
    VIEWS_ASC("Views--min to max"),
    VIEWS_DESC("Views--max to min")
}