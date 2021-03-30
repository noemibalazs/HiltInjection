package com.noemi.android.hiltinjection.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Artwork(
    @Json(name = "id") val id: Int,
    @Json(name = "tags") val tags: String,
    @Json(name = "user") val user: String,
    @Json(name = "largeImageURL") val url: String
) {
    override fun toString(): String {
        return "Artwork: id=$id, tags='$tags', user='$user', url='$url'"
    }
}