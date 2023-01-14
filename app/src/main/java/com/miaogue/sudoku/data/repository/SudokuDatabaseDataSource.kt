package com.miaogue.sudoku.data.repository

import com.miaogue.sudoku.data.database.AppDatabase
import com.miaogue.sudoku.data.database.entity.Config
import com.miaogue.sudoku.data.database.entity.Progress

/**
 * 数据源 - 本地数据库
 */
class SudokuDatabaseDataSource {

    private val configDao = AppDatabase.get().configDao()
    private val progressDao = AppDatabase.get().progressDao()

    // 获取等级
    fun getLive(): Int{
        val value = configDao.getValue("live")
        return value?.toInt() ?: 1
    }

    // 保存等级
    fun saveLive(live: Int){
        var config = configDao.getConfig("live")
        if (config == null) {
            config = Config(0, "live", live.toString())
            configDao.insert(config)
        } else {
            config.value = live.toString()
            configDao.update(config)
        }
    }

    // 保存记录
    fun saveProgress(live: Int, data: List<Int>?, baseData: List<Int>?, time: Long, complete: Boolean = false){
        var progress = progressDao.findProgressByLive(live)
        if (data != null && baseData != null){
            if (progress == null) {
                // 新增记录
                progress = Progress(0, live, baseData, data, time, complete)
                progressDao.insert(progress)
            } else {
                // 更新记录
                progress.data = data
                progress.baseData = baseData
                progress.time = time
                progress.complete = complete
                progressDao.update(progress)
            }
        }else {
            // 更新记录时间
            progress?.let {
                progress.time = time
                progress.complete = complete
                progressDao.update(progress)
            }
        }
    }

    fun getProgress(live: Int, complete: Boolean = false): Progress? {
        return progressDao.findProgressByLive(live, complete)
    }


}