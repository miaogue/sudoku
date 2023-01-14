package com.miaogue.sudoku.data.repository

import com.miaogue.sudoku.data.database.entity.Progress


/**
 * 数据层
 */
class SudokuRepository {

    private val sudokuDatabaseDataSource = SudokuDatabaseDataSource()
    // 获取等级
    val live: Int = sudokuDatabaseDataSource.getLive()

    // 保存等级
    fun saveLive(live: Int){
        sudokuDatabaseDataSource.saveLive(live)
    }

    // 保存记录
    fun saveProgress(live: Int, data: List<Int>?, baseData: List<Int>?, time: Long, complete: Boolean = false){
        sudokuDatabaseDataSource.saveProgress(live, data, baseData, time, complete)
    }

    // 查询记录
    fun getProgress(live: Int, complete: Boolean = false): Progress? {
        return sudokuDatabaseDataSource.getProgress(live, complete)
    }

}