package com.noemi.android.hiltinjection.helper

import com.noemi.android.hiltinjection.data.Artwork
import com.noemi.android.hiltinjection.room.ArtEntity
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun mapArtEntity2Artwork(artEntity: ArtEntity): Artwork {
        return Artwork(
            id = artEntity.id,
            tags = artEntity.tag,
            user = artEntity.user,
            url = artEntity.url
        )
    }

    fun mapArtwork2ArtEntity(artwork: Artwork): ArtEntity {
        return ArtEntity(
            id = artwork.id,
            url = artwork.url,
            tag = artwork.tags,
            user = artwork.user
        )
    }
}