package com.miaogue.sudoku

import android.app.Application
import kotlin.properties.Delegates

class MainApplication : Application(){

    companion object{
        var instance: MainApplication by Delegates.notNull()
        fun instance() = instance
    }


    override fun onCreate() {
        super.onCreate()

        instance = this
    }

}