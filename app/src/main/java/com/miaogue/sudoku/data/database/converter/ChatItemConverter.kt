package com.miaogue.sudoku.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChatItemConverter {

    @TypeConverter
    fun objectToString(list: List<Int>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToObject(json: String): List<Int> {
        val listType = object : TypeToken<List<Int>>(){}.type
        return Gson().fromJson(json, listType)
    }
}