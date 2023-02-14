package com.spectrum.moviedbapp.data.utils

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val DATABASE_NAME = "movie_db"
    const val IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/original"

    const val FAVOURITE = 1
    const val NOT_FAVOURITE = 2

    const val ARG_MOVIE_ID = "movieId"
    const val ARG_SEARCH_QUERY = "search_query"

    public enum class MovieListType {
        NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING
    }

}