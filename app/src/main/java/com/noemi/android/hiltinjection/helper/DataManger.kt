package com.noemi.android.hiltinjection.helper

import android.content.Context
import com.noemi.android.hiltinjection.util.MY_PREFERENCES
import com.noemi.android.hiltinjection.util.TAG_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataManger @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE)

    fun saveTag(tag: String) {
        sharedPreferences.edit().putString(TAG_KEY, tag).apply()
    }

    fun getTag(): String {
        return sharedPreferences.getString(TAG_KEY, "") ?: ""
    }
}