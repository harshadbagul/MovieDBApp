package com.spectrum.moviedbapp.data.network.model

import com.spectrum.moviedbapp.data.utils.Constants

data class MovieDetail(
    var parentTitle:String?=null,
    var data: String? = null,
    var type:Int = Constants.PARENT,
    var subList : MutableList<ChildData> = ArrayList(),
    var isExpanded: Boolean = false
)

data class ChildData(val childTitle:String)
