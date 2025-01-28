package com.example.shows.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.example.shows.details.presentation.DetailsViewModel
import com.example.shows.movieList.data.local.movie.MediaDatabase
import com.example.shows.movieList.data.remote.MediaApi
import com.example.shows.movieList.data.repository.MediaListRepositoryImpl
import com.example.shows.movieList.domain.repository.MediaListRepository
import com.example.shows.movieList.presentation.MediaListViewModel
import com.example.shows.network.NetworkObserver
import com.example.shows.network.NetworkViewModel
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    val appModule = module {
        single {
            createMediaApi()
        }
        single {
            createMediaDatabase(get())
        }
        single<MediaListRepository> {
            MediaListRepositoryImpl(get(), get())
        }
        viewModel {
            MediaListViewModel(get())
        }


    }
}
private fun createMediaApi(): MediaApi {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = okhttp3.OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(MediaApi.BASE_URL)
        .client(client)
        .build()
        .create(MediaApi::class.java)
}

private fun createMediaDatabase(app: Application): MediaDatabase {
    return Room.databaseBuilder(
        app,
        MediaDatabase::class.java,
        "mediadb.db"
    ).build()
}
