package com.example.shows.movieList.presentation

sealed interface MediaListUiEvent {
    data class Paginate(val category: String):MediaListUiEvent
    object Navigate:MediaListUiEvent
}