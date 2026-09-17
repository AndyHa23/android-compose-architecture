package com.andyha.musicdomain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize


@Entity(tableName = SavedPlayList.TABLE_NAME, primaryKeys = ["playList"])
@Parcelize
data class SavedPlayList(

    @ColumnInfo(name = "playList")
    val playList: List<SavedMusicItem>,

    @ColumnInfo(name = "playingItem")
    val playingItemId: String,

    @ColumnInfo(name = "playingPosition")
    val playingPosition: Long,

    @ColumnInfo(name = "repeatMode")
    val repeatMode: Int,

    @ColumnInfo(name = "shuffleMode")
    val shuffleMode: Boolean,
) : Parcelable {
    companion object {
        const val TABLE_NAME = "SavedPlayList"
    }
}
