package com.noemi.android.hiltinjection.network

import com.noemi.android.hiltinjection.data.ArtworkBlock
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtAPI {

    @GET("?")
    fun getArtWorks(
        @Query("key") key: String,
        @Query("order") order: String,
        @Query("per_page") number: Int,
        @Query("image_type") photo: String,
        @Query("q") query: String
    ): Call<ArtworkBlock>
}