package com.example.shows.di

import com.example.shows.movieList.data.repository.MediaListRepositoryImpl
import com.example.shows.movieList.domain.repository.MediaListRepository
import com.example.shows.network.NetworkObserver
import com.example.shows.network.NetworkViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module{

    single<MediaListRepository> { MediaListRepositoryImpl(get(), get()) }
    single { NetworkObserver(get()) }
    viewModel {
        NetworkViewModel(get())
    }
}