package com.noemi.android.hiltinjection.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.noemi.android.hiltinjection.R
import com.noemi.android.hiltinjection.data.Artwork
import com.noemi.android.hiltinjection.databinding.ItemArtworkBinding

class ArtworkVH(
    private val binding: ItemArtworkBinding,
    private val artListener: (Artwork) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(artwork: Artwork) {
        binding.apply {
            artTag.text = artwork.tags

            Glide.with(artAvatar.context)
                .load(artwork.url)
                .error(R.drawable.tiger)
                .placeholder(R.drawable.tiger)
                .into(artAvatar)

            clItemContainer.setOnClickListener {
                artListener(artwork)
            }
        }
    }
}