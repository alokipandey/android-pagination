package com.example.mytube.repository.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Insert
    fun savePosts()

    @Query("select * from VIDEO_POSTS")
    fun getPosts()
}