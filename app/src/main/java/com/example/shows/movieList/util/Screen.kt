package com.example.shows.movieList.util

sealed class Screen(val rout: String) {
    object Home : Screen("main")
    object MovieList : Screen("movie")
    object TvShowList : Screen("tv")
    object Details : Screen("details")
}