package com.miaogue.sudoku.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miaogue.sudoku.data.database.entity.Progress
import com.miaogue.sudoku.data.repository.SudokuRepository

/**
 * 界面响应组件
 */
class SudokuViewModel : ViewModel() {

    private val sudokuRepository = SudokuRepository()


    /**
     * Live 等级处理
     */
    private val live: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also {
            it.value = sudokuRepository.live
        }
    }

    fun getLive(): LiveData<Int> {
        return live
    }

    fun getLiveInt(): Int{
        return live.value!!
    }

    fun plusLive() {
        if (live.value!! < 4) {
            live.value = live.value?.plus(1)
            live.value?.let { sudokuRepository.saveLive(it) }
            loadProgress()
        }
    }

    fun minusLive() {
        if (live.value!! > 1) {
            live.value = live.value?.minus(1)
            live.value?.let { sudokuRepository.saveLive(it) }
            loadProgress()
        }
    }


    /**
     * 游戏进度和记录处理
     */
    private val progress = MutableLiveData<Progress>()
    fun getProgress(): LiveData<Progress> {
        return progress
    }
    fun loadProgress(){
        live.value?.let { it ->
            val p = sudokuRepository.getProgress(it)
            progress.value = p
        }
    }
    fun clearProgress(){
        progress.value = null
    }

    fun saveProgress(data: List<Int>, baseData: List<Int>, time: Long, complete: Boolean = false){
        live.value?.let { sudokuRepository.saveProgress(it, data, baseData, time, complete) }
    }

    fun setTime(time: Long){
        live.value?.let { sudokuRepository.saveProgress(it, null, null, time) }
    }

}