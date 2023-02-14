package com.spectrum.moviedbapp.data.network.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("dates") var dates: Dates? = Dates(),
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<Results> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null,
    @SerializedName("genres") var genres: List<Genres>? = null,
    val movieDetail: Results? = null
)

@Entity
data class Genres(
    @PrimaryKey @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null
)

@Entity
data class Results(
    @PrimaryKey @SerializedName("id") var id: Int? = null,
    @SerializedName("adult") var adult: Boolean? = null,
    @SerializedName("backdrop_path") var backdropPath: String? = null,
    @Ignore @SerializedName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
    @SerializedName("original_language") var originalLanguage: String? = null,
    @SerializedName("original_title") var originalTitle: String? = null,
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("popularity") var popularity: Double? = null,
    @SerializedName("poster_path") var posterPath: String? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("video") var video: Boolean? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    @Ignore @SerializedName("genres") var genres: List<Genres>? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("tagline") var tagline: String? = null,
    @Ignore @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguages>? = null,
    @Ignore var genreNames: List<String> = arrayListOf(),
)

data class SpokenLanguages(
    @SerializedName("english_name") var english_name: String? = null,
    @SerializedName("iso_639_1") var iso: String? = null,
    @SerializedName("name") var name: String? = null
)

data class Dates(
    @SerializedName("maximum") var maximum: String? = null,
    @SerializedName("minimum") var minimum: String? = null
)
