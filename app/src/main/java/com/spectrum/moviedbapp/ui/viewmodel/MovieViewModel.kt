package com.spectrum.moviedbapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spectrum.moviedbapp.data.database.MovieDatabase
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.network.service.MovieState
import com.spectrum.moviedbapp.data.repository.MovieRepository
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


    suspend fun fetchNowPlaying(page: Int) {
        movieRepository.fetchNowPlaying(page).flowOn(Dispatchers.IO)
            .onStart { emit(MovieState.Loading) }.onEach { _nowPlayingStateFlow.value = it }
            .launchIn(viewModelScope)

    }

    suspend fun mapGenresToMovie(results: ArrayList<Results>): ArrayList<Results> {
        var newResults: ArrayList<Results> = arrayListOf()

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

}