package com.spectrum.moviedbapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spectrum.moviedbapp.data.database.MovieDatabase
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.network.service.MovieState
import com.spectrum.moviedbapp.data.repository.MovieRepository
import com.spectrum.moviedbapp.data.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository, private val movieDatabase: MovieDatabase
) : ViewModel() {

    // --> Movie.Loading > show loading while calling service
    // --> Movie.Success > Emit values on success
    // --> Movie.Error > Emit error object & handle it in fragment

    private val _nowPlayingStateFlow = MutableStateFlow<MovieState>(MovieState.Loading)
    val nowPlayingStateFlow: StateFlow<MovieState>
        get() = _nowPlayingStateFlow

    private val _popularStateFlow = MutableStateFlow<MovieState>(MovieState.Loading)
    val popularStateFlow: StateFlow<MovieState>
        get() = _popularStateFlow

    private val _topRatedStateFlow = MutableStateFlow<MovieState>(MovieState.Loading)
    val topRatedStateFlow: StateFlow<MovieState>
        get() = _topRatedStateFlow

    private val _upcomingStateFlow = MutableStateFlow<MovieState>(MovieState.Loading)
    val upcomingStateFlow: StateFlow<MovieState>
        get() = _upcomingStateFlow

    private val _movieDetailStateFlow = MutableStateFlow<MovieState>(MovieState.Loading)
    val movieDetailStateFlow: StateFlow<MovieState>
        get() = _movieDetailStateFlow

    private val _searchResultStateFlow = MutableStateFlow<MovieState>(MovieState.Loading)
    val searchResultStateFlow: StateFlow<MovieState>
        get() = _searchResultStateFlow


    suspend fun fetchNowPlaying(page: Int) {
        movieRepository.fetchMovies(page, Constants.MovieListType.NOW_PLAYING)
            .flowOn(Dispatchers.IO)
            .onStart { emit(MovieState.Loading) }
            .onEach { _nowPlayingStateFlow.value = it }
            .launchIn(viewModelScope)
    }

    suspend fun fetchPopular(page: Int) {
        movieRepository.fetchMovies(page, Constants.MovieListType.POPULAR)
            .flowOn(Dispatchers.IO)
            .onStart { emit(MovieState.Loading) }
            .onEach { _popularStateFlow.value = it }
            .launchIn(viewModelScope)
    }

    suspend fun fetchTopRated(page: Int) {
        movieRepository.fetchMovies(page, Constants.MovieListType.TOP_RATED)
            .flowOn(Dispatchers.IO)
            .onStart { emit(MovieState.Loading) }
            .onEach { _topRatedStateFlow.value = it }
            .launchIn(viewModelScope)
    }

    suspend fun fetchUpcoming(page: Int) {
        movieRepository.fetchMovies(page, Constants.MovieListType.UPCOMING)
            .flowOn(Dispatchers.IO)
            .onStart { emit(MovieState.Loading) }
            .onEach { _upcomingStateFlow.value = it }
            .launchIn(viewModelScope)
    }

    suspend fun fetchMovieDetail(movieId: String) {
        movieRepository.fetchMovieDetail(movieId)
            .flowOn(Dispatchers.IO)
            .onStart { emit(MovieState.Loading) }
            .onEach { _movieDetailStateFlow.value = it }
            .launchIn(viewModelScope)
    }

    suspend fun fetchQueryResult(query: String) {
        movieRepository.fetchQueryResults(query)
            .flowOn(Dispatchers.IO)
            .onStart { emit(MovieState.Loading) }
            .onEach { _searchResultStateFlow.value = it }
            .launchIn(viewModelScope)
    }


    suspend fun mapGenresToMovie(results: ArrayList<Results>): ArrayList<Results> {
        val newResults: ArrayList<Results> = arrayListOf()

        val allGenres = movieDatabase.movieDao.getAllGenres()
        results.forEachIndexed { index, result ->
            val list = allGenres.filter {
                result.genreIds.contains(it.id)
            }.map { it.name ?: "" }.toList()

            result.genreNames = list
            newResults.add(result)
        }

        return newResults
    }

    suspend fun fetchGenres() {
        movieRepository.getGenres()
    }


    suspend fun addToMovieToFavourite(result: Results?){
        result?.let { movieDatabase.movieDao.insertMovie(it) }
    }

    suspend fun removeMovieFromFavourite(result: Results?){
        result?.let { movieDatabase.movieDao.deleteMovie(it) }
    }

    suspend fun isMovieFavourite(movieId: String): Boolean {
       val allFavouriteMovies = movieDatabase.movieDao.getAllFavouriteMovies()
       val isMovieFavourite = allFavouriteMovies.any { it.id.toString() == movieId }

       return isMovieFavourite
    }

    suspend fun getAllFavouriteMovies(): List<Results> {
        return movieDatabase.movieDao.getAllFavouriteMovies()
    }

}