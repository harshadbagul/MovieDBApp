package com.spectrum.moviedbapp.data.network.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(@SerializedName("code") val code: String,
                         @SerializedName("info") val info: String)