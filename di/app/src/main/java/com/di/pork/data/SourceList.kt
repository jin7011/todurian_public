package com.di.pork.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SourceList(
    @SerializedName("result")
    @Expose
    val sourceList: ArrayList<Source>,
)

data class Source(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("link")
    @Expose
    val link: String,
    @SerializedName("content")
    @Expose
    val content: String
)

