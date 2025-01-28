package com.example.shows.movieList.domain.repository

import com.example.shows.movieList.domain.model.Media
import com.example.shows.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface MediaListRepository {
    suspend fun getMediaList(
        fetchFromRemote: Boolean,//this is for when we close/pause the app it is no need to call remote api
        // rather than fetch from local database
        category: String,
        page: Int
    ):Flow<Resource<List<Media>>>

    suspend fun getMedia(id:Int):Flow<Resource<Media>>
}