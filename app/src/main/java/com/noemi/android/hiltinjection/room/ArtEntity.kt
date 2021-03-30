package com.noemi.android.hiltinjection.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noemi.android.hiltinjection.util.ART_TABLE

@Entity(tableName = ART_TABLE)
data class ArtEntity(
    @PrimaryKey
    val tag: String,
    val id: Int,
    val user: String,
    val url: String
) {
    override fun toString(): String {
        return "ArtEntity: tag='$tag', id=$id, user='$user', url='$url'"
    }
}