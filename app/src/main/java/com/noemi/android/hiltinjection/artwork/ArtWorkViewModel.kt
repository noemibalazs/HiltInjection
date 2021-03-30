package com.noemi.android.hiltinjection.artwork

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noemi.android.hiltinjection.datasourcelocal.ArtDataSource
import com.noemi.android.hiltinjection.helper.DataManger
import com.noemi.android.hiltinjection.room.ArtEntity
import com.orhanobut.logger.Logger
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class ArtWorkViewModel @ViewModelInject constructor(
    private val artDataSource: ArtDataSource,
    private val dataManger: DataManger
) : ViewModel() {

    fun getEntityFromDB(): LiveData<ArtEntity> {
        Logger.d("AWWM", "getEntityFromDB()")
        return artDataSource.getArtByTag(dataManger.getTag())
    }
}