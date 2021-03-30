package com.noemi.android.hiltinjection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.noemi.android.hiltinjection.R
import com.noemi.android.hiltinjection.data.Artwork
import com.noemi.android.hiltinjection.databinding.ItemArtworkBinding

class ArtworkAdapter(private val artListener: (Artwork) -> Unit) :
    ListAdapter<Artwork, ArtworkVH>(ArtworkDifUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemArtworkBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_artwork, parent, false)
        return ArtworkVH(binding, artListener)
    }

    override fun onBindViewHolder(holder: ArtworkVH, position: Int) {
        holder.onBind(getItem(position))
    }
}