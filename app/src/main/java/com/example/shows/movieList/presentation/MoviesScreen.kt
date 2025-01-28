package com.example.shows.movieList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shows.movieList.presentation.components.MediaItem
import com.example.shows.movieList.util.Category

@Composable
fun MoviesScreen(
    mediaListState:MediaListState,
    navController: NavHostController,
    onEvent:(MediaListUiEvent)->Unit
)
{
    if (mediaListState.movies.isEmpty()){
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
            ){
                items(10){
                    ShimmerMediaItemPlaceholderTV()
                }
            }
        }
    }
    else{
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(mediaListState.movies.size){ index ->
                MediaItem(
                    media = mediaListState.movies[index],
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (index >= mediaListState.movies.size - 1 && !mediaListState.isLoading) {
                    onEvent(MediaListUiEvent.Paginate(Category.MOVIE))

                }

            }

        }

    }

}
