package com.example.shows.movieList.data.remote

import com.example.shows.movieList.data.remote.response.MediaListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaApi {


    @GET("discover/{mediaType}")
    suspend fun getMediaList(
        @Path("mediaType") mediaType: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ):MediaListDto


    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "xxxx"
    }
}