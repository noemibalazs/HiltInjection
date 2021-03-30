package com.noemi.android.hiltinjection.datasourceremote

import com.noemi.android.hiltinjection.data.ArtworkBlock
import retrofit2.Call

interface ArtRemoteData {

    fun getRemoteArtworks(order: String, search: String): Call<ArtworkBlock>
}