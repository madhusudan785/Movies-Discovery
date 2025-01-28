package com.example.shows.movieList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shows.movieList.presentation.components.MediaItem
import com.example.shows.movieList.util.Category
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun TvShowsScreen(
    mediaListState:MediaListState,
    navController: NavHostController,
    onEvent:(MediaListUiEvent)->Unit
)
{
    if (mediaListState.tv_shows.isEmpty()){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 items in a row
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ){
            items(10){
                ShimmerMediaItemPlaceholderTV()
            }
        }
    }
    else{
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(mediaListState.tv_shows.size){ index ->
                MediaItem(
                    media = mediaListState.tv_shows[index],
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (index >= mediaListState.tv_shows.size - 1 && !mediaListState.isLoading) {
                    onEvent(MediaListUiEvent.Paginate(Category.TV_SHOWS))

                }

            }

        }

    }
}
@Composable
fun ShimmerMediaItemPlaceholderTV() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(160.dp) // Set your desired placeholder size
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .placeholder(visible = true, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), highlight = PlaceholderHighlight.shimmer())
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .placeholder(visible = true, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), highlight = PlaceholderHighlight.shimmer())
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(0.5f) // Adjust width for varying lengths
                .clip(RoundedCornerShape(8.dp))
                .placeholder(visible = true, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), highlight = PlaceholderHighlight.shimmer())
        )
    }
}