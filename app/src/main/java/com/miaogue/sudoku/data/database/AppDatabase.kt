package com.miaogue.sudoku.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.miaogue.sudoku.MainApplication
import com.miaogue.sudoku.data.database.dao.ConfigDao
import com.miaogue.sudoku.data.database.dao.ProgressDap
import com.miaogue.sudoku.data.database.entity.Config
import com.miaogue.sudoku.data.database.entity.Progress

@Database(entities = [Config::class, Progress::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun progressDao(): ProgressDap

    companion object {
        private var instance: AppDatabase? = null


        @Synchronized
        fun get(): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    MainApplication.instance(),
                    AppDatabase::class.java,
                    "sudoku"
                ).allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}