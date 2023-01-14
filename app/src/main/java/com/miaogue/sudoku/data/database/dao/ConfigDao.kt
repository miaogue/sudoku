package com.miaogue.sudoku.data.database.dao

import androidx.room.*
import com.miaogue.sudoku.data.database.entity.Config

@Dao
interface ConfigDao {

    @Query("SELECT value FROM config WHERE `key`=:key")
    fun getValue(key: String): String?

    @Query("SELECT * FROM config WHERE `key`=:key")
    fun getConfig(key: String): Config?

    @Insert
    fun insert(vararg configs: Config)

    @Update
    fun update(config: Config)

    @Delete
    fun delete(config: Config)
}