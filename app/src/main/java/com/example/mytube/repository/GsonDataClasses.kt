package com.example.mytube.repository

import kotlin.collections.ArrayList

data class Video(
    var id:String,
    var thumbnail_image:String,
    var event_name:String,
    var event_date:String,
    var views:Int,
    var likes:Int,
    var shares:Int,
    var isCollapsed:Boolean = true){
    init { }
}

data class VideoResponse(
    var posts:ArrayList<Video>,
    var page:Int){}

//data class VideoResponseLiveData(){}
