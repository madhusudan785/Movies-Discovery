package com.example.shows.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shows.movieList.presentation.MediaListUiEvent
import com.example.shows.movieList.presentation.MediaListViewModel
import com.example.shows.movieList.presentation.MoviesScreen
import com.example.shows.movieList.presentation.TvShowsScreen
import com.example.shows.movieList.util.Screen
import com.example.shows.network.NetworkViewModel
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController,networkViewModel: NetworkViewModel) {
    val mediaListViewModel: MediaListViewModel = getViewModel()
    val mediaListState=mediaListViewModel.mediaListState.collectAsState().value
    val bottomNavController= rememberNavController()

    val isConnected by networkViewModel.isConnected.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            val result = snackbarHostState.showSnackbar(
                message = "No internet connection. Please check your connection.",
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                mediaListViewModel.retryFetchingData()
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar ={
            BottomNavigationBar(
                bottomNavController = bottomNavController, onEvent = mediaListViewModel::onEvent
            )
        },
        topBar = {
            TopAppBar(title = {
                Text(
                    text = if (mediaListState.isCurrentMovieScreen )
                       "Movies"
                    else
                        "TV Shows",
                        fontSize = 20.sp
                )
            },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )
            )
        })
    {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            NavHost(navController = bottomNavController,
                startDestination = Screen.MovieList.rout)
            {
                composable(Screen.MovieList.rout){
                    MoviesScreen(
                        navController = navController,
                        mediaListState = mediaListState,
                        onEvent = mediaListViewModel::onEvent)
                }
                composable(Screen.TvShowList.rout){
                    TvShowsScreen(navController = navController,
                        mediaListState = mediaListState,
                        onEvent = mediaListViewModel::onEvent)
                }
            }
        }
    }
    
}

@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController,
    onEvent: (MediaListUiEvent) -> Unit
) {
    val items = listOf(
        BottomItem(
            title = "Movies",
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = "TV Shows",
            icon = Icons.Rounded.Upcoming
        )
    )

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        items.forEachIndexed { index, bottomItem ->
            val isSelected = when (index) {
                0 -> bottomNavController.currentDestination?.route == Screen.MovieList.rout
                1 -> bottomNavController.currentDestination?.route == Screen.TvShowList.rout
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    when (index) {
                        0 -> {
                            if (bottomNavController.currentDestination?.route != Screen.MovieList.rout) {
                                onEvent(MediaListUiEvent.Navigate)
                                bottomNavController.navigate(Screen.MovieList.rout) {
                                    popUpTo(Screen.MovieList.rout) { inclusive = false }
                                }
                            }
                        }
                        1 -> {
                            if (bottomNavController.currentDestination?.route != Screen.TvShowList.rout) {
                                onEvent(MediaListUiEvent.Navigate)
                                bottomNavController.navigate(Screen.TvShowList.rout) {
                                    popUpTo(Screen.MovieList.rout) { inclusive = false }
                                }
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = bottomItem.icon,
                        contentDescription = bottomItem.title,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(
                        bottomItem.title,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }
    }
}


data class BottomItem(
    val title : String,
    val icon:ImageVector
)