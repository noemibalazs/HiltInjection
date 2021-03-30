package com.noemi.android.hiltinjection.latest

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noemi.android.hiltinjection.data.*
import com.noemi.android.hiltinjection.datasourceremote.ArtRemoteData
import com.noemi.android.hiltinjection.helper.OnClickEvent
import com.noemi.android.hiltinjection.util.*
import com.orhanobut.logger.Logger
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.*
import retrofit2.*

@FragmentScoped
class LatestViewModel @ViewModelInject constructor(private val artRemoteData: ArtRemoteData) :
    ViewModel() {

    val failureError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    val onDoneClicked = MutableLiveData<OnClickEvent>()
    val shouldInformAboutLength = MutableLiveData<Boolean>()

    val mutableDefaultLatestList = MutableLiveData<MutableList<Artwork>>()
    val mutableSearchedLatestList = MutableLiveData<MutableList<Artwork>>()

    init {
        getDefaultLatestArtList()
    }

    private fun getDefaultLatestArtList() {
        Logger.d("getDefaultLatestArtList")
        loading.value = true
        GlobalScope.launch(Dispatchers.IO) {
            val response = artRemoteData.getRemoteArtworks(LATEST, LATEST_DEFAULT)
            withContext(Dispatchers.Main) {
                try {
                    response.enqueue(object : Callback<ArtworkBlock> {
                        override fun onResponse(
                            call: Call<ArtworkBlock>,
                            response: Response<ArtworkBlock>
                        ) {
                            loading.value = false
                            if (!response.isSuccessful) {
                                Logger.e("onResponse error code: ${response.code()}")
                                failureError.value = true
                            }

                            if (response.isSuccessful) {
                                Logger.d("onResponse successful")
                                response.body()?.let {
                                    mutableDefaultLatestList.value = it.hits
                                }
                            }
                        }

                        override fun onFailure(call: Call<ArtworkBlock>, t: Throwable) {
                            Logger.e("onFailure message: ${t.message}")
                            loading.value = false
                            failureError.value = true
                        }
                    })

                } catch (e: Exception) {
                    Logger.e("Error getting default latest art list: ${e.message}")
                    failureError.value = true
                }
            }
        }
    }

    fun onSearchClicked() {
        Logger.d("onSearchClicked()")
        onDoneClicked.value = OnClickEvent.LATEST_DONE
    }

    fun showInfoAboutLengthOfSearchedText(show: Boolean) {
        Logger.d("showInfoAboutLength()")
        shouldInformAboutLength.value = show
    }

    fun latestArtListBasedOnSearch(search: String) {
        Logger.d("getSearchedLatestArtList")
        loading.value = true
        GlobalScope.launch(Dispatchers.IO) {
            val response = artRemoteData.getRemoteArtworks(LATEST, search)
            withContext(Dispatchers.Main) {
                try {
                    response.enqueue(object : Callback<ArtworkBlock> {
                        override fun onResponse(
                            call: Call<ArtworkBlock>,
                            response: Response<ArtworkBlock>
                        ) {
                            loading.value = false
                            if (!response.isSuccessful) {
                                Logger.e("onResponse error code: ${response.code()}")
                                failureError.value = true
                            }

                            if (response.isSuccessful) {
                                Logger.d("onResponse successful")
                                response.body()?.let {
                                    mutableSearchedLatestList.value = it.hits
                                }
                            }
                        }

                        override fun onFailure(call: Call<ArtworkBlock>, t: Throwable) {
                            Logger.e("onFailure message: ${t.message}")
                            loading.value = false
                            failureError.value = true
                        }
                    })

                } catch (e: Exception) {
                    Logger.e("Error getting the latest searched art list: ${e.message}")
                    failureError.value = true
                }
            }
        }
    }
}