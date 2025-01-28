package com.example.shows.movieList.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.shows.movieList.data.remote.MediaApi
import com.example.shows.movieList.domain.model.Media
import com.example.shows.movieList.util.RatingBar
import com.example.shows.movieList.util.Screen
import com.example.shows.movieList.util.getAverageColor

@Composable
fun MediaItem(
    media: Media,
    navController: NavHostController
) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MediaApi.IMAGE_BASE_URL+media.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val defaultColor=MaterialTheme.colorScheme.secondaryContainer
    var dominantColor by remember {
        mutableStateOf(defaultColor)
    }
    Column(modifier = Modifier
        .wrapContentHeight()
        .width(200.dp)
        .padding(8.dp)
        .clip(RoundedCornerShape(28.dp))
        .background(
            Brush.verticalGradient(
                listOf(
                    MaterialTheme.colorScheme.secondaryContainer,
                    dominantColor,
                )

            )
        )
        .clickable {
            navController.navigate(Screen.Details.rout + "/${media.id}")
        })
    {
        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(250.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = media.title
                )
            }
        }
        if (imageState is AsyncImagePainter.State.Success) {
            dominantColor= getAverageColor(
                imageBitmap = imageState.result.drawable.toBitmap().asImageBitmap()
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(250.dp)
                    .clip(RoundedCornerShape(22.dp)),
                painter = imageState.painter,
                contentDescription = media.title,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = media.title.ifEmpty { media.name },
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            color = Color.White,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top= 4.dp, bottom = 12.dp)
        ){
                RatingBar(
                    modifier = Modifier.size(18.dp),
                    rating = media.vote_average/2
                    )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = media.vote_average.toString().take(3),
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
        }



    }

}