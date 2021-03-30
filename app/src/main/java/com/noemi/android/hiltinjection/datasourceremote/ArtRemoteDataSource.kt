package com.noemi.android.hiltinjection.datasourceremote

import com.noemi.android.hiltinjection.data.ArtworkBlock
import com.noemi.android.hiltinjection.network.ArtAPI
import com.noemi.android.hiltinjection.util.IMAGE_TYPE
import com.noemi.android.hiltinjection.util.KEY
import com.orhanobut.logger.Logger
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtRemoteDataSource @Inject constructor(private val artAPI: ArtAPI) : ArtRemoteData {

    override fun getRemoteArtworks(order: String, search: String): Call<ArtworkBlock> {
        Logger.d("getRemoteArtworks")
        return artAPI.getArtWorks(KEY, order, 50, IMAGE_TYPE, search)
    }
}