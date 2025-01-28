package com.example.shows.di

import com.example.shows.movieList.data.repository.MediaListRepositoryImpl
import com.example.shows.movieList.domain.repository.MediaListRepository
import org.koin.dsl.module


val repositoryModule = module {
    single<MediaListRepository> { MediaListRepositoryImpl(get(), get()) }
}