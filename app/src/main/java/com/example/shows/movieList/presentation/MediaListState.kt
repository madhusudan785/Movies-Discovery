package com.example.shows.movieList.presentation

import com.example.shows.movieList.domain.model.Media

data class MediaListState(
    val isLoading :Boolean=false,
    val moviePage:Int=1,
    val tvShowPage:Int=1,

    val isCurrentMovieScreen:Boolean=true,

    val movies:List<Media> = emptyList(),
    val tv_shows:List<Media> = emptyList(),
)