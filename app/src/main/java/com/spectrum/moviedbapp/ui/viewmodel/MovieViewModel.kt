package com.spectrum.moviedbapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spectrum.moviedbapp.data.database.MovieDatabase
import com.spectrum.moviedbapp.data.network.service.MovieState
import com.spectrum.moviedbapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val movieDatabase: MovieDatabase
) : ViewModel() {

    // --> CurrencyState.Loading > show loading while calling service
    // --> CurrencyState.Success > Emit values on success
    // --> CurrencyState.Error > Emit error object & handle it in fragment

    private val _nowPlayingStateFlow = MutableStateFlow<MovieState>(MovieState.Loading)
    val nowPlayingStateFlow: StateFlow<MovieState>
        get() = _nowPlayingStateFlow


    suspend fun fetchNowPlaying(page: Int) {
        movieRepository.fetchNowPlaying(page)
            .flowOn(Dispatchers.IO)
            .onStart { emit(MovieState.Loading) }
            .onEach { _nowPlayingStateFlow.value = it }
            .launchIn(viewModelScope)

    }

}