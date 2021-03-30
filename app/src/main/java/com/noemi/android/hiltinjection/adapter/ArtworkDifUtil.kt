package com.noemi.android.hiltinjection.adapter

import androidx.recyclerview.widget.DiffUtil
import com.noemi.android.hiltinjection.data.Artwork

class ArtworkDifUtil : DiffUtil.ItemCallback<Artwork>() {

    override fun areItemsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
        return oldItem == newItem
    }
}