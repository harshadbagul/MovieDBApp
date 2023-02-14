package com.spectrum.moviedbapp.data.repository


import com.spectrum.moviedbapp.BuildConfig
import com.spectrum.moviedbapp.data.database.MovieDatabase
import com.spectrum.moviedbapp.data.network.model.MovieResponse
import com.spectrum.moviedbapp.data.network.service.MovieService
import com.spectrum.moviedbapp.data.network.service.MovieState
import com.spectrum.moviedbapp.data.utils.Constants
import com.spectrum.moviedbapp.data.utils.Constants.MOVIE_QUERY_PARAM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {

    suspend fun fetchMovies(page:Int, movieType: Constants.MovieListType): Flow<MovieState> {
        return flow {
            val response = getNowPlaying(page, movieType)
            response?.let { resp ->
                emit(MovieState.Success(resp))
            } ?: run {
                emit(MovieState.Error(null))
            }

        }.flowOn(Dispatchers.IO)
    }


    fun fetchMovieDetail(movieId: String): Flow<MovieState> {
        val query = mapOf(
            MOVIE_QUERY_PARAM to movieId
        )

        return flow {
            val response = movieService.getMovieDetail(movieId, BuildConfig.API_KEY)
            response.let { resp ->

                val movieResponse = MovieResponse(movieDetail = resp)
                emit(MovieState.Success(movieResponse))

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

    private suspend fun getNowPlaying(page: Int, movieType: Constants.MovieListType): MovieResponse? {
        return try {
            withContext(Dispatchers.IO) {
                when(movieType){
                    Constants.MovieListType.NOW_PLAYING -> {
                        movieService.getNowPlaying(BuildConfig.API_KEY, page = page)
                    }
                    Constants.MovieListType.TOP_RATED -> {
                        movieService.getTopRated(BuildConfig.API_KEY, page = page)
                    }
                    Constants.MovieListType.POPULAR -> {
                        movieService.getPopular(BuildConfig.API_KEY, page = page)
                    }
                    Constants.MovieListType.UPCOMING -> {
                        movieService.getUpcoming(BuildConfig.API_KEY, page = page)
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            return null
        }
    }



}