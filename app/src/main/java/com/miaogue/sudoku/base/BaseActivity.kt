package com.miaogue.sudoku.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun setContentView(view: View?) {
        super.setContentView(view)
        bindEvent()
    }

    abstract fun bindEvent()
}