package com.miaogue.sudoku.view.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miaogue.sudoku.base.BaseFragment
import com.miaogue.sudoku.data.viewmodel.SudokuViewModel
import com.miaogue.sudoku.databinding.FragmentSudokuBinding
import com.miaogue.sudoku.widget.Algorithm
import com.miaogue.sudoku.widget.SudokuView

class SudokuFragment : BaseFragment() {

    private var _binding: FragmentSudokuBinding? = null
    private val binding get() = _binding!!

    private val model: SudokuViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSudokuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 绑定导航栏
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)


    }

    override fun bindEvent() {
        binding.one.setOnClickListener { binding.sudoku.setSelectNumber(1) }
        binding.two.setOnClickListener { binding.sudoku.setSelectNumber(2) }
        binding.three.setOnClickListener { binding.sudoku.setSelectNumber(3) }
        binding.four.setOnClickListener { binding.sudoku.setSelectNumber(4) }
        binding.five.setOnClickListener { binding.sudoku.setSelectNumber(5) }
        binding.six.setOnClickListener { binding.sudoku.setSelectNumber(6) }
        binding.seven.setOnClickListener { binding.sudoku.setSelectNumber(7) }
        binding.eight.setOnClickListener { binding.sudoku.setSelectNumber(8) }
        binding.nine.setOnClickListener { binding.sudoku.setSelectNumber(9) }
        binding.delete.setOnClickListener { binding.sudoku.setSelectNumber(0) }

        binding.sudoku.setOnCallListener(object : SudokuView.OnCallListener {
            override fun onResult(data: List<Int>, baseData: List<Int>, isComplete: Boolean) {
                // 完成
                if (isComplete) {
                    MaterialAlertDialogBuilder(requireActivity())
                        .setMessage("已完成")
                        .setNegativeButton("确定") { dialog, _ ->
                            run { dialog.dismiss() }
                        }.show()
                    binding.timer.stop()
                }
                val time = (SystemClock.elapsedRealtime() - binding.timer.base)
                model.saveProgress(data, baseData, time, isComplete)
            }
        })

        // 计时器事件
        binding.timer.setOnChronometerTickListener {
            model.setTime(SystemClock.elapsedRealtime() - it.base)
        }

        model.getProgress().observe(this) {
            if (it == null) {
                binding.sudoku.init(Algorithm().getExamSudo(model.getLiveInt()))
                binding.timer.base = SystemClock.elapsedRealtime()
                binding.timer.start()
            } else {
                binding.sudoku.init(it.data, it.baseData)
                binding.timer.base = SystemClock.elapsedRealtime() - it.time
                binding.timer.start()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}