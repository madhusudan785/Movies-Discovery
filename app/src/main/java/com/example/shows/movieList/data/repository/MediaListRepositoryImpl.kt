package com.example.shows.movieList.data.repository

import com.example.shows.movieList.data.local.movie.MediaDatabase
import com.example.shows.movieList.data.mappers.toMovie
import com.example.shows.movieList.data.mappers.toMovieEntity
import com.example.shows.movieList.data.remote.MediaApi
import com.example.shows.movieList.domain.model.Media
import com.example.shows.movieList.domain.repository.MediaListRepository
import com.example.shows.movieList.util.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class MediaListRepositoryImpl (
    private val api: MediaApi,
    private val db: MediaDatabase
) :MediaListRepository{
    override suspend fun getMediaList(
        fetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Media>>> {

        return flow {
            emit(Resource.Loading(true))

            val localMovieList = db.mediaDao.getMovieByCategory(category)

            val shouldJustLoadFromLocal = !fetchFromRemote && localMovieList.isNotEmpty()

            if (shouldJustLoadFromLocal) {
                emit(Resource.Success(data = localMovieList.map { movieEntity ->
                    movieEntity.toMovie(category)
                }))
                emit(Resource.Loading(false))

                return@flow
            }

            val loadMovieFromApi=try{
                api.getMediaList(category,page)
            }
            catch (e:IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                return@flow
            }
            catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                return@flow

            }
            catch (e:Exception){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                return@flow
            }

            val movieEntities=loadMovieFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)

                }
            }

            db.mediaDao.upsertMoviesList(movieEntities)

            emit(Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))

            emit(Resource.Loading(false))

        }

    }

    override suspend fun getMedia(id:Int): Flow<Resource<Media>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity=db.mediaDao.getMovieById(id)

            if (movieEntity != null){
                emit(
                    Resource.Success(movieEntity.toMovie(movieEntity.category))
                )
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("No Such movie is present"))

            emit(Resource.Loading(false))

        }
    }
}