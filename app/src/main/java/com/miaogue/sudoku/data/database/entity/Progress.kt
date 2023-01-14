package com.miaogue.sudoku.data.database.entity

import androidx.room.*
import com.miaogue.sudoku.data.database.converter.ChatItemConverter

@Entity
@TypeConverters(ChatItemConverter::class)
data class Progress(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "live") val live: Int,
    @ColumnInfo(name = "baseData") var baseData: List<Int>,
    @ColumnInfo(name = "data") var data: List<Int>,
    @ColumnInfo(name = "time") var time: Long,
    @ColumnInfo(name = "complete") var complete: Boolean = false
    )