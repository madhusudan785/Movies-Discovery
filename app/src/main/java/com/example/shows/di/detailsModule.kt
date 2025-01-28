package com.example.shows.di

import androidx.lifecycle.SavedStateHandle
import com.example.shows.details.presentation.DetailsViewModel
import com.example.shows.movieList.data.repository.MediaListRepositoryImpl
import com.example.shows.movieList.domain.repository.MediaListRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {

    single<MediaListRepository> { MediaListRepositoryImpl(get(), get()) }

    viewModel { (savedStateHandle: SavedStateHandle) ->
        DetailsViewModel(savedStateHandle, get())
    }
}