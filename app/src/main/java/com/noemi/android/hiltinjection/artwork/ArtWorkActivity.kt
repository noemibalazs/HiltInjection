package com.noemi.android.hiltinjection.artwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.noemi.android.hiltinjection.R
import com.noemi.android.hiltinjection.databinding.ActivityArtWorkBinding
import com.noemi.android.hiltinjection.room.ArtEntity
import com.noemi.android.hiltinjection.util.ENTITY_TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArtWorkActivity @Inject constructor() : AppCompatActivity() {

    private val TAG = ArtWorkActivity::class.java.simpleName

    private lateinit var biding: ActivityArtWorkBinding
    private val artWorkViewModel: ArtWorkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = DataBindingUtil.setContentView(this, R.layout.activity_art_work)

        initBinding()
        initObservers()

        intent?.let {
            if (it.action == ENTITY_TAG) {
                informUserWithSnack()
            }
        }
    }

    private fun initBinding() {
        Log.d(TAG, "initBinding()")
        biding.artWorkVM = artWorkViewModel
    }

    private fun initObservers() {
        Log.d(TAG, "initObservers()")
        artWorkViewModel.getEntityFromDB().observe(this, { entity ->
            populateUI(entity)
        })
    }

    private fun populateUI(entity: ArtEntity) {
        Log.d(TAG, "populateUI()")
        Glide.with(this).load(entity.url).placeholder(R.drawable.tiger).error(R.drawable.tiger)
            .into(biding.ivArtWork)
    }

    private fun informUserWithSnack() {
        Log.d(TAG, "informUserWithSnack()")
        val snack =
            Snackbar.make(biding.clContainer, getString(R.string.snack_toast), Snackbar.LENGTH_LONG)
        val view = snack.view
        val text = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        text.setTextColor(getColor(R.color.colorPrimary))
        snack.show()
    }
}