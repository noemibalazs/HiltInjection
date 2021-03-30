package com.noemi.android.hiltinjection.datasourcelocal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noemi.android.hiltinjection.room.ArtDAO
import com.noemi.android.hiltinjection.room.ArtEntity
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtLocalDataSource @Inject constructor(private val artDAO: ArtDAO) : ArtDataSource {

    override fun addArt2DB(entity: ArtEntity) {
        Log.d("ALDS", "Add art entity to data base")
        GlobalScope.launch(Dispatchers.IO) {
            artDAO.addArt2DB(entity)
        }
    }

    override fun getArtByTag(tag: String): LiveData<ArtEntity> {
        Log.d("ALDS", "Get art entity by id")
        val mutableArtEntity = MutableLiveData<ArtEntity>()
        GlobalScope.launch(Dispatchers.IO) {
            val art = artDAO.getArtByTag(tag)
            withContext(Dispatchers.Main) {
                try {
                    mutableArtEntity.value = art
                } catch (e: Exception) {
                    Logger.e("Error getting art by id: ${e.message}")
                }
            }
        }
        return mutableArtEntity
    }

    override fun getArtList(): LiveData<MutableList<ArtEntity>> {
        Log.d("ALDS", "Get art list from data base")
        val mutableList = MutableLiveData<MutableList<ArtEntity>>()
        GlobalScope.launch(Dispatchers.IO) {
            val artList = artDAO.getArtList()
            withContext(Dispatchers.Main) {
                try {
                    mutableList.value = artList
                } catch (e: Exception) {
                    Logger.e("Error getting the art list from DB")
                }
            }
        }
        return mutableList
    }

    override fun getSearchedArtList(tag: String): LiveData<MutableList<ArtEntity>> {
        Log.d("ALDS", "Get searched art list from data base: $tag")
        val mutableList = MutableLiveData<MutableList<ArtEntity>>()
        GlobalScope.launch(Dispatchers.IO) {
            val searchedList = artDAO.getSearchedArtList(tag)
            withContext(Dispatchers.Main) {
                try {
                    mutableList.value = searchedList
                } catch (e: Exception) {
                    Logger.e("Error something went wrong")
                }
            }
        }
        return mutableList
    }
}
