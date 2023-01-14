package com.miaogue.sudoku.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.miaogue.sudoku.R
import com.miaogue.sudoku.base.BaseFragment
import com.miaogue.sudoku.databinding.FragmentMainBinding
import com.miaogue.sudoku.data.viewmodel.SudokuViewModel

class MainFragment : BaseFragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val model: SudokuViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 绑定导航栏
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        model.loadProgress()
    }

    override fun bindEvent() {
        // 减低游戏难度
        binding.liveLeft.setOnClickListener {
            model.minusLive()
        }

        // 增加游戏难度
        binding.liveRight.setOnClickListener {
            model.plusLive()
        }

        // 创建新游戏
        binding.newGame.setOnClickListener {
            model.clearProgress()
            findNavController().navigate(R.id.action_MainFragment_to_SudokuFragment)
        }

        // 继续游戏
        binding.resume.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_SudokuFragment)
        }


        model.getLive().observe(viewLifecycleOwner) {
            if (it != null) setLiveTitle(it)
        }

        model.getProgress().observe(this) {
            if (it == null) {
                binding.resume.visibility = View.GONE
            } else {
                binding.resume.visibility = View.VISIBLE
            }
        }
    }

    // 修改等级标题
    private fun setLiveTitle(live: Int) {
        when (live) {
            1 -> binding.liveTitle.text = "容易"
            2 -> binding.liveTitle.text = "中等"
            3 -> binding.liveTitle.text = "困难"
            4 -> binding.liveTitle.text = "专家"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}