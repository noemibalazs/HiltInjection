package com.noemi.android.hiltinjection.favorite

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.noemi.android.hiltinjection.datasourcelocal.ArtDataSource
import com.noemi.android.hiltinjection.room.ArtEntity
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class FavoriteViewModel @ViewModelInject constructor(
    private val artDataSource: ArtDataSource
) :
    ViewModel() {

    init {
        getDefaultFavoriteList()
    }

    fun getDefaultFavoriteList(): LiveData<MutableList<ArtEntity>> {
        Log.d("FVM", "getDefaultFavoriteList()")
        return artDataSource.getArtList()
    }

    fun getSearchedFavoriteList(tag: String): LiveData<MutableList<ArtEntity>> {
        Log.d("FVM", "getSearchedFavoriteList - tag is: $tag")
        return artDataSource.getSearchedArtList(tag)
    }
}