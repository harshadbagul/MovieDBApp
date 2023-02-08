package com.spectrum.moviedbapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spectrum.moviedbapp.data.database.MovieDatabase
import com.spectrum.moviedbapp.data.repository.MovieRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MovieViewModelFactory @Inject constructor(
    private val repository: MovieRepository,
    private val database: MovieDatabase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(repository,database) as T
    }
}