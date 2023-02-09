package com.spectrum.moviedbapp.data.network.service

import com.spectrum.moviedbapp.data.network.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MovieService {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") api_key: String,
                                 @Query("page") page: Int): MovieResponse

    @GET("genre/movie/list")
    suspend fun getGenre(@Query("api_key") api_key: String): MovieResponse

}