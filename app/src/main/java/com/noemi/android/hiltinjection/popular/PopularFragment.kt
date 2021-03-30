package com.noemi.android.hiltinjection.popular

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.noemi.android.hiltinjection.R
import com.noemi.android.hiltinjection.adapter.ArtworkAdapter
import com.noemi.android.hiltinjection.data.Artwork
import com.noemi.android.hiltinjection.databinding.FragmentPoularBinding
import com.noemi.android.hiltinjection.artwork.ArtWorkActivity
import com.noemi.android.hiltinjection.datasourcelocal.ArtDataSource
import com.noemi.android.hiltinjection.helper.*
import com.noemi.android.hiltinjection.room.ArtEntity
import com.noemi.android.hiltinjection.util.ENTITY_TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PopularFragment @Inject constructor() : Fragment() {

    private val TAG = PopularFragment::class.java.simpleName

    private val popularViewModel: PopularViewModel by viewModels()

    @Inject
    lateinit var dataManager: DataManger

    @Inject
    lateinit var mapper: Mapper

    @Inject
    lateinit var artDataSource: ArtDataSource

    private lateinit var binding: FragmentPoularBinding
    private lateinit var artworkAdapter: ArtworkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_poular, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initObservers()
    }

    private fun initBindings() {
        Log.d(TAG, "initBindings()")
        binding.popularViewModel = popularViewModel
        artworkAdapter = ArtworkAdapter(this::onArtWorkClicked)
        binding.rvPopular.adapter = artworkAdapter
    }

    private fun onArtWorkClicked(artwork: Artwork) {
        Log.d(TAG, "onArtWorkClicked()")
        dataManager.saveTag(artwork.tags)
        addEntityToDB(mapper.mapArtwork2ArtEntity(artwork))
        openArtWorkActivity()
    }

    private fun addEntityToDB(entity: ArtEntity) {
        Log.d(TAG, "addEntityToDB")
        artDataSource.addArt2DB(entity)
    }

    private fun openArtWorkActivity() {
        Log.d(TAG, "openArtWorkActivity()")
        activity?.let {
            val intent = Intent(it, ArtWorkActivity::class.java)
            intent.action = ENTITY_TAG
            startActivity(intent)
        }
    }

    private fun initObservers() {
        Log.d(TAG, "initObservers()")
        popularViewModel.mutableDefaultPopularList.observe(viewLifecycleOwner, {
            artworkAdapter.submitList(it)
        })

        popularViewModel.failureError.observe(viewLifecycleOwner, { failure ->
            informUserAboutError(failure)
        })

        popularViewModel.loading.observe(viewLifecycleOwner, { loading ->
            showHideProgressBar(loading)
        })

        popularViewModel.onDoneClicked.observe(viewLifecycleOwner, { event ->
            onDoneClicked(event)
        })

        popularViewModel.shouldInformAboutLength.observe(viewLifecycleOwner, { show ->
            if (!show)
                informAboutTheSearchedTextLength()
        })

        popularViewModel.mutableSearchedPopularList.observe(viewLifecycleOwner, {
            artworkAdapter.submitList(null)
            artworkAdapter.submitList(it)
        })
    }

    private fun informUserAboutError(isError: Boolean) {
        Log.d(TAG, "informUserAboutError()")
        if (isError) {
            activity?.applicationContext?.let {
                Toast.makeText(it, getString(R.string.error_toast), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showHideProgressBar(show: Boolean) {
        Log.d(TAG, "showHideProgressBar()")
        binding.pbPopular.isVisible = show
        binding.rvPopular.isVisible = !show
    }

    private fun onDoneClicked(event: OnClickEvent) {
        Log.d(TAG, "onDoneClicked")
        if (event == OnClickEvent.POPULAR_DONE) {
            if (isValidLength()) {
                popularViewModel.popularArtListBasedOnSearch(binding.searchForArt.text.toString())
                binding.searchForArt.setText("")
            }
            if (!isValidLength()) {
                popularViewModel.showInfoAboutLengthOfSearchedText(!isValidLength())
            }
        }
    }

    private fun isValidLength(): Boolean {
        Log.d(TAG, "isValidLength()")
        return binding.searchForArt.text.toString().trim().length > 3
    }

    private fun informAboutTheSearchedTextLength() {
        Log.d(TAG, "informAboutTheSearchedTextLength()")
        activity?.applicationContext?.let {
            Toast.makeText(it, getString(R.string.search_toast), Toast.LENGTH_LONG).show()
        }
    }
}