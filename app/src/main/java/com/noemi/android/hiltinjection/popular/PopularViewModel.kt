package com.noemi.android.hiltinjection.popular

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
class PopularViewModel @ViewModelInject constructor(private val artRemoteData: ArtRemoteData) :
    ViewModel() {

    val failureError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    val mutableDefaultPopularList = MutableLiveData<MutableList<Artwork>>()
    val mutableSearchedPopularList = MutableLiveData<MutableList<Artwork>>()

    val onDoneClicked = MutableLiveData<OnClickEvent>()
    val shouldInformAboutLength = MutableLiveData<Boolean>()

    init {
        getDefaultList()
    }

    private fun getDefaultList() {
        Logger.d("getDefaultList")
        loading.value = true
        GlobalScope.launch(Dispatchers.IO) {
            val response = artRemoteData.getRemoteArtworks(POPULAR, POPULAR_DEFAULT)
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
                                    mutableDefaultPopularList.value = it.hits
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
                    Logger.e("Error getting default popular art list: ${e.message}")
                }
            }
        }
    }

    fun onSearchClicked() {
        Logger.d("onSearchClicked()")
        onDoneClicked.value = OnClickEvent.POPULAR_DONE
    }

    fun showInfoAboutLengthOfSearchedText(show: Boolean) {
        Logger.d("showInfoAboutLength()")
        shouldInformAboutLength.value = show
    }

    fun popularArtListBasedOnSearch(search: String) {
        Logger.d("getSearchedPopularArtList")
        loading.value = true
        GlobalScope.launch(Dispatchers.IO) {
            val response = artRemoteData.getRemoteArtworks(POPULAR, search)
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
                                    mutableSearchedPopularList.value = it.hits
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
                    Logger.e("Error getting the searched popular art list: ${e.message}")
                }
            }
        }
    }
}