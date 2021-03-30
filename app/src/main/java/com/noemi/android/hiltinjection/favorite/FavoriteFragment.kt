package com.noemi.android.hiltinjection.favorite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.noemi.android.hiltinjection.R
import com.noemi.android.hiltinjection.adapter.*
import com.noemi.android.hiltinjection.artwork.ArtWorkActivity
import com.noemi.android.hiltinjection.data.Artwork
import com.noemi.android.hiltinjection.databinding.FragmentFavoriteBinding
import com.noemi.android.hiltinjection.helper.*
import com.noemi.android.hiltinjection.room.ArtEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment @Inject constructor() : Fragment() {

    private val TAG = FavoriteFragment::class.java.simpleName

    private val favoriteVM: FavoriteViewModel by viewModels()

    @Inject
    lateinit var dataManager: DataManger

    @Inject
    lateinit var mapper: Mapper

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var artworkAdapter: ArtworkAdapter
    private var showOrHide = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBiding()
        initObservers()
        initSearchObserver()
    }

    private fun initBiding() {
        Log.d(TAG, "initBiding()")
        binding.favoriteVM = favoriteVM
        artworkAdapter = ArtworkAdapter(this::onArtWorkClicked)
        binding.rvFavorite.adapter = artworkAdapter
    }

    private fun onArtWorkClicked(artwork: Artwork) {
        Log.d(TAG, "onArtWorkClicked()")
        dataManager.saveTag(artwork.tags)
        openArtWorkActivity()
    }

    private fun openArtWorkActivity() {
        Log.d(TAG, "openArtWorkActivity()")
        activity?.let {
            startActivity(Intent(it, ArtWorkActivity::class.java))
        }
    }

    private fun initObservers() {
        Log.d(TAG, "initObservers()")
        favoriteVM.getDefaultFavoriteList().observe(viewLifecycleOwner, {
            artworkAdapter.submitList(mapEntityList2ArtWorkList(it))
        })
    }

    private fun initSearchObserver() {
        Log.d(TAG, "initSearchObserver()")
        binding.searchForArt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    if (isValid()) {
                        getSearchedList(it.toString().trim())
                        binding.rvFavorite.isVisible = false
                        showOrHide = true
                    }
                }
            }
        })
    }

    private fun isValid(): Boolean {
        Log.d(TAG, "isValid()")
        return binding.searchForArt.text.toString().trim().length >= 3
    }

    private fun getSearchedList(tag: String) {
        favoriteVM.getSearchedFavoriteList(tag).observe(viewLifecycleOwner, {
            getSearchedArts(it)
        })
    }

    private fun getSearchedArts(entityList: MutableList<ArtEntity>) {
        Log.d(TAG, "getSearchedArts()")
        if (entityList.isNullOrEmpty() && showOrHide) {
            showOption2User(true)
        }

        if (!entityList.isNullOrEmpty() && showOrHide) {
            showOption2User(false)
            loadSearchedList(entityList)
        }
    }

    private fun loadSearchedList(entityList: MutableList<ArtEntity>) {
        Log.d(TAG, "loadSearchedList()")

        if (!entityList.isNullOrEmpty())
            showOption2User(false)

        binding.searchForArt.setText("")
        binding.rvFavorite.isVisible = true
        artworkAdapter.submitList(null)
        artworkAdapter.submitList(mapEntityList2ArtWorkList(entityList))
        showOrHide = false
    }

    private fun mapEntityList2ArtWorkList(entityList: MutableList<ArtEntity>): MutableList<Artwork> {
        Log.d(TAG, "mapEntityList2ArtWorkList()")
        val artWorkList = mutableListOf<Artwork>()
        entityList.map { entity -> artWorkList.add(mapper.mapArtEntity2Artwork(entity)) }
        return artWorkList
    }

    private fun showOption2User(show: Boolean) {
        Log.d(TAG, "showOption2User()")
        showOrHide = false
        val snack =
            Snackbar.make(clFavorite, getString(R.string.empty_snack_toast), Snackbar.LENGTH_SHORT)
        val text = snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        text.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        snack.setAction(getString(R.string.snack_undo)) {
            binding.searchForArt.setText("")
            binding.rvFavorite.isVisible = true
            favoriteVM.getDefaultFavoriteList()
        }
        if (show) snack.show() else snack.dismiss()
    }
}