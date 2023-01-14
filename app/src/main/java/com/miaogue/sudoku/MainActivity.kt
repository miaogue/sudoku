package com.miaogue.sudoku

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.activity.viewModels
import androidx.navigation.NavController
import com.miaogue.sudoku.base.BaseActivity
import com.miaogue.sudoku.databinding.ActivityMainBinding
import com.miaogue.sudoku.data.viewmodel.SudokuViewModel

class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val model: SudokuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_main)
    }

    override fun bindEvent() {

    }
}