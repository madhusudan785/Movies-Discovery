package com.example.shows.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shows.movieList.domain.repository.MediaListRepository
import com.example.shows.movieList.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mediaListRepository: MediaListRepository
) : ViewModel() {

        private val mediaId = savedStateHandle.get<Int>("movieId")!!
        private var _detailState=MutableStateFlow(DetailState())
        val detailState=_detailState.asStateFlow()

    init {
        getMedia(mediaId)
    }

    private fun getMedia(id: Int) {
        viewModelScope.launch {
            _detailState.update {
                it.copy(isLoading = true)
            }

            mediaListRepository.getMedia(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let {
                            media->
                            _detailState.update {
                                it.copy(media = media)
                            }
                        }


                    }
                    is Resource.Loading -> {
                        _detailState.update {
                            it.copy(result.isLoading)
                        }
                    }
                }
            }
        }
    }

}