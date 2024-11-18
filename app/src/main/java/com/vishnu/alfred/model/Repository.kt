package com.vishnu.alfred.model

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: Long,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("html_url") val htmlUrl: String,
    val description: String?,
    val fork: Boolean,
    val url: String,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    val language: String?,
)