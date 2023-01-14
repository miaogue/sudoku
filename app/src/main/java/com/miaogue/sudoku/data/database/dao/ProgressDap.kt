package com.miaogue.sudoku.data.database.dao

import androidx.room.*
import com.miaogue.sudoku.data.database.entity.Progress

@Dao
interface ProgressDap {

    @Insert
    fun insert(vararg progress: Progress)

    @Update
    fun update(vararg progress: Progress)

    @Delete
    fun delete(vararg progress: Progress)

    @Query("select * from progress where live=:live and complete=:complete")
    fun findProgressByLive(live: Int, complete: Boolean = false): Progress?


}