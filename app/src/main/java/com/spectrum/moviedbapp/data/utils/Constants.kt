package com.spectrum.moviedbapp.data.utils

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val DATABASE_NAME = "movie_db"
    const val IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/original"

    const val SPLASH = "/"
    const val QUERY_PARAM_BASE = "base"
    const val MOVIE_QUERY_PARAM = "movie-id"

    const val PARENT = 0
    const val CHILD = 1

    const val FAVOURITE = 1
    const val NOT_FAVOURITE = 2

    public enum class MovieListType {
        NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING
    }

}