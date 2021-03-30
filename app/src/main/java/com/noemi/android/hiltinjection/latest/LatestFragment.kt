package com.noemi.android.hiltinjection.latest

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
import com.noemi.android.hiltinjection.databinding.FragmentLatestBinding
import com.noemi.android.hiltinjection.artwork.ArtWorkActivity
import com.noemi.android.hiltinjection.datasourcelocal.ArtDataSource
import com.noemi.android.hiltinjection.helper.*
import com.noemi.android.hiltinjection.room.*
import com.noemi.android.hiltinjection.util.ENTITY_TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LatestFragment @Inject constructor() : Fragment() {

    private val TAG = LatestFragment::class.java.simpleName

    private val latestViewModel: LatestViewModel by viewModels()

    @Inject
    lateinit var artDataSource: ArtDataSource

    @Inject
    lateinit var dataManger: DataManger

    @Inject
    lateinit var mapper: Mapper

    private lateinit var binding: FragmentLatestBinding
    private lateinit var artAdapter: ArtworkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_latest, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initObservers()
    }

    private fun initBinding() {
        Log.d(TAG, "initBinding()")
        binding.latestViewModel = latestViewModel
        artAdapter = ArtworkAdapter(this::onArtClicked)
        binding.rvLatest.adapter = artAdapter
    }

    private fun onArtClicked(artwork: Artwork) {
        Log.d(TAG, "onArtClicked() - id: ${artwork.id}")
        dataManger.saveTag(artwork.tags)
        addArtEntity2DB(mapper.mapArtwork2ArtEntity(artwork))
        openArtWorkActivity()
    }

    private fun addArtEntity2DB(artEntity: ArtEntity) {
        Log.d(TAG, "addArtEntity2DB")
        artDataSource.addArt2DB(artEntity)
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
        latestViewModel.mutableDefaultLatestList.observe(viewLifecycleOwner, {
            artAdapter.submitList(it)
        })

        latestViewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            showHideProgressBar(isLoading)
        })

        latestViewModel.failureError.observe(viewLifecycleOwner, { isError ->
            informUserAboutError(isError)
        })

        latestViewModel.onDoneClicked.observe(viewLifecycleOwner, { event ->
            onDoneClicked(event)
        })

        latestViewModel.shouldInformAboutLength.observe(viewLifecycleOwner, { show ->
            if (!show)
                informUserAboutTheSearchedTextLength()
        })

        latestViewModel.mutableSearchedLatestList.observe(viewLifecycleOwner, {
            artAdapter.submitList(null)
            artAdapter.submitList(it)
        })
    }

    private fun showHideProgressBar(isLoading: Boolean) {
        Log.d(TAG, "showHideProgressBar()")
        binding.pbLatest.isVisible = isLoading
        binding.rvLatest.isVisible = !isLoading
    }

    private fun informUserAboutError(isError: Boolean) {
        if (isError) {
            activity?.applicationContext.let {
                Toast.makeText(it, getString(R.string.error_toast), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onDoneClicked(event: OnClickEvent) {
        Log.d(TAG, "onDoneClicked()")
        if (event == OnClickEvent.LATEST_DONE) {
            if (isValidLength()) {
                latestViewModel.latestArtListBasedOnSearch(binding.searchForArt.text.toString())
                binding.searchForArt.setText("")
            }

            if (!isValidLength()) {
                latestViewModel.showInfoAboutLengthOfSearchedText(!isValidLength())
            }
        }
    }

    private fun isValidLength(): Boolean {
        Log.d(TAG, "isValidLength()")
        return binding.searchForArt.text.toString().trim().length > 3
    }

    private fun informUserAboutTheSearchedTextLength() {
        Log.d(TAG, "informUserAboutTheSearchedTextLength()")
        activity?.applicationContext?.let {
            Toast.makeText(it, R.string.search_toast, Toast.LENGTH_LONG).show()
        }
    }
}
