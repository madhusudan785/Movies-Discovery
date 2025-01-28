package com.example.shows.movieList.data.local.movie

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [MediaEntity::class],
    version = 1
)
abstract class MediaDatabase:RoomDatabase() {
    abstract val mediaDao:MediaDao
}