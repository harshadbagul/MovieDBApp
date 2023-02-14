package com.spectrum.moviedbapp.data.network.service

import com.spectrum.moviedbapp.data.network.model.MovieResponse
import com.spectrum.moviedbapp.data.network.model.Results
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MovieService {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") api_key: String,
                                 @Query("page") page: Int): MovieResponse

    @GET("movie/popular")
    suspend fun getPopular(@Query("api_key") api_key: String,
                              @Query("page") page: Int): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("api_key") api_key: String,
                           @Query("page") page: Int): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(@Query("api_key") api_key: String,
                           @Query("page") page: Int): MovieResponse

    @GET("genre/movie/list")
    suspend fun getGenre(@Query("api_key") api_key: String): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String): Results

}