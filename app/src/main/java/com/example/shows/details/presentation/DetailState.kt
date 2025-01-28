package com.example.shows.details.presentation

import com.example.shows.movieList.domain.model.Media

data class DetailState(
    val isLoading:Boolean=false,
    val media: Media?=null,
    )
