package com.noemi.android.hiltinjection.datasourcelocal

import androidx.lifecycle.LiveData
import com.noemi.android.hiltinjection.room.ArtEntity

interface ArtDataSource {

    fun addArt2DB(entity: ArtEntity)
    fun getArtByTag(tag: String): LiveData<ArtEntity>
    fun getArtList(): LiveData<MutableList<ArtEntity>>
    fun getSearchedArtList(tag: String): LiveData<MutableList<ArtEntity>>
}