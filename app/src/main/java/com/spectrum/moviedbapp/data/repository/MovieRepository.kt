package com.spectrum.moviedbapp.data.repository


import com.spectrum.moviedbapp.BuildConfig
import com.spectrum.moviedbapp.data.database.MovieDatabase
import com.spectrum.moviedbapp.data.network.model.MovieResponse
import com.spectrum.moviedbapp.data.network.service.MovieService
import com.spectrum.moviedbapp.data.network.service.MovieState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {

    suspend fun fetchNowPlaying(page:Int): kotlinx.coroutines.flow.Flow<MovieState> {
        return flow {
            val response = getNowPlaying(page)
            response?.let { resp ->
                emit(MovieState.Success(resp))
            } ?: run {
                emit(MovieState.Error(null))
            }

        }.flowOn(Dispatchers.IO)
    }


    suspend fun getGenres() {
        val response = withContext(Dispatchers.IO) {
            movieService.getGenre(BuildConfig.API_KEY)
        }
        response.genres?.let { genres ->
            movieDatabase.movieDao.insertGenres(genres)
        }
    }

    private suspend fun getNowPlaying(page:Int): MovieResponse? {
        return try {
            withContext(Dispatchers.IO) {
                movieService.getNowPlaying(BuildConfig.API_KEY, page = page)
            }
        }catch (e: Exception){
            e.printStackTrace()
            return null
        }
    }


}