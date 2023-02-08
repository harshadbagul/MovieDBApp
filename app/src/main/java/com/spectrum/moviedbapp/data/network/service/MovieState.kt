package com.spectrum.moviedbapp.data.network.service

import com.spectrum.moviedbapp.data.network.model.MovieResponse
import com.spectrum.moviedbapp.data.network.model.ErrorResponse

sealed class MovieState {
    object Loading : MovieState()
    data class Success(val response: MovieResponse) : MovieState()
    data class Error(val error: ErrorResponse?) : MovieState()
}