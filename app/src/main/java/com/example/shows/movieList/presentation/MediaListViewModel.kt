package com.example.shows.movieList.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shows.movieList.data.remote.MediaApi
import com.example.shows.movieList.domain.repository.MediaListRepository
import com.example.shows.movieList.util.Category
import com.example.shows.movieList.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MediaListViewModel(
    private val mediaRepository: MediaListRepository
) : ViewModel() {
    private var _mediaListState = MutableStateFlow(MediaListState())
    val mediaListState = _mediaListState.asStateFlow()

    init {
        getMovieList(false)
        getTvShowList(false)
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun onEvent(event: MediaListUiEvent) {
        when (event) {
            MediaListUiEvent.Navigate -> {
                _mediaListState.update {
                    it.copy(
                        isCurrentMovieScreen = !mediaListState.value.isCurrentMovieScreen
                    )
                }
            }
            is MediaListUiEvent.Paginate -> {
                when (event.category) {
                    Category.MOVIE -> getMovieList(true)
                    Category.TV_SHOWS -> getTvShowList(true)
                }
            }
        }
    }

    private fun getMovieList(fetchFromRemote: Boolean) {
        viewModelScope.launch {
            _mediaListState.update { it.copy(isLoading = true) }

            mediaRepository.getMediaList(
                fetchFromRemote,
                Category.MOVIE,
                _mediaListState.value.moviePage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { movieList ->
                            _mediaListState.update {
                                it.copy(
                                    movies = mediaListState.value.movies + movieList.shuffled(),
                                    moviePage = mediaListState.value.moviePage + 1
                                )
                            }
                        }
                    }
                    is Resource.Error -> _mediaListState.update {
                        it.copy(isLoading = false)
                    }
                    is Resource.Loading -> _mediaListState.update {
                        it.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }
    private fun getTvShowList(fetchFromRemote: Boolean) {

        viewModelScope.launch {
            _mediaListState.update {
                it.copy(isLoading = true)
            }

            mediaRepository.getMediaList(
                fetchFromRemote,
                Category.TV_SHOWS,
                _mediaListState.value.tvShowPage
            ).collectLatest { result ->
                when(result){
                    is Resource.Error ->_mediaListState.update {
                        it.copy(isLoading = false)
                    }


                    is Resource.Success ->
                        result.data?.let {
                                mediaList->
                            _mediaListState.update {
                                it.copy(tv_shows = mediaListState.value.tv_shows+mediaList.shuffled(),
                                    tvShowPage = mediaListState.value.tvShowPage+1
                                )

                            }
                        }

                    is Resource.Loading -> _mediaListState.update {
                        it.copy(isLoading = result.isLoading)
                    }
                }
            }

        }
    }

    fun retryFetchingData() {
        getMovieList(true)
        getTvShowList(true)
    }
}
