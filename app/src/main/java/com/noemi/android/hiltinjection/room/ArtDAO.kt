package com.noemi.android.hiltinjection.room

import androidx.room.*

@Dao
interface ArtDAO {

    @Query("SELECT * FROM ART_TABLE")
    fun getArtList(): MutableList<ArtEntity>

    @Query("SELECT * FROM ART_TABLE WHERE tag =:tag")
    fun getArtByTag(tag: String): ArtEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArt2DB(artwork: ArtEntity)

    @Query("SELECT * FROM ART_TABLE WHERE tag =:tag")
    fun getSearchedArtList(tag: String): MutableList<ArtEntity>
}