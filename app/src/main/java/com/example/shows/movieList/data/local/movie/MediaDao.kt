package com.example.shows.movieList.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface MediaDao {

    @Upsert
    suspend fun upsertMoviesList(movieList: List<MediaEntity>)

    @Query("SELECT * FROM MediaEntity  WHERE id = :id")
    suspend fun getMovieById(id: Int): MediaEntity?

    @Query("SELECT * FROM MediaEntity WHERE category = :category")
    suspend fun getMovieByCategory(category: String): List<MediaEntity>
}