package com.miaogue.sudoku.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Config(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "value") var value: String
)