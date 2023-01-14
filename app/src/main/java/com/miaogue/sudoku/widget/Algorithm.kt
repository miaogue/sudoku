package com.miaogue.sudoku.widget

import java.util.*

class Algorithm {


    private fun randomSudo(): IntArray {
        var sudo: IntArray
        val remainingNumbers = TreeSet<Int>()
        for (n in 1..9) {
            remainingNumbers.add(n)
        }
        do {
            sudo = IntArray(81)
        } while (!performTraverse(sudo, remainingNumbers))
        return sudo
    }

    private fun performTraverse(
        sudo: IntArray,
        remainingNumbers: TreeSet<Int>
    ): Boolean {
        val random = Random()
        val start = System.currentTimeMillis()
        for (i in 0..8) {
            var j = 0
            while (j < 9) {
                if (System.currentTimeMillis() - start > 30) {
                    return false
                }
                val cellRemainNumbers = TreeSet(remainingNumbers)
                for (x in 0 until j) {
                    if (sudo[i * 9 + x] != 0) {
                        cellRemainNumbers.remove(sudo[i * 9 + x])
                    }
                }
                for (y in 0 until i) {
                    if (sudo[y * 9 + j] != 0) {
                        cellRemainNumbers.remove(sudo[y * 9 + j])
                    }
                }
                val currentRow = i / 3
                val currentColumn = j / 3
                for (rowX in currentRow * 3 until currentRow * 3 + 3) {
                    for (columnY in currentColumn * 3 until currentColumn * 3 + 3) {
                        if (sudo[rowX * 9 + columnY] != 0) {
                            cellRemainNumbers.remove(sudo[rowX * 9 + columnY])
                        }
                    }
                }
                if (cellRemainNumbers.size == 0) {
                    j = 0
                    for (p in 0..8) {
                        sudo[i * 9 + p] = 0
                    }
                } else {
                    val lst: List<Int> = ArrayList(cellRemainNumbers)
                    val index = random.nextInt(cellRemainNumbers.size)
                    val cell = lst[index]
                    sudo[i * 9 + j] = cell
                    j++
                }
            }
        }
        return true
    }

    fun getExamSudo(type: Int): IntArray {
        return getExamSudo(randomSudo(), type)
    }

    private fun getExamSudo(sudo: IntArray, type: Int): IntArray {
        val topicSudo = IntArray(81)
        var blankCount = getBlankCount(type)
        val random = Random()
        while (blankCount != 0) {
            val randomNumber = random.nextInt(81)
            val row = randomNumber / 9
            val column = randomNumber % 9
            if (topicSudo[row * 9 + column] == 0) {
                topicSudo[row * 9 + column] = 10
                blankCount--
            }
        }
        for (i in 0..8) {
            for (j in 0..8) {
                if (topicSudo[i * 9 + j] == 10) {
                    topicSudo[i * 9 + j] = sudo[i * 9 + j]
                }
            }
        }
        return topicSudo
    }

    private fun getBlankCount(type: Int): Int {
        return when (type) {
            1 -> {
                (39..44).random() // 容易
            }
            2 -> {
                (30..37).random() // 中等
            }
            3 -> {
                (24..28).random() // 困难
            }
            else -> {
                (20..23).random() // 专家
            }
        }
    }

    fun checkSudoEqually(sudo: IntArray, targetSudo: IntArray): Boolean {
        for (i in 0..8) {
            for (j in 0..8) {
                if (sudo[i * 9 + j] != targetSudo[i * 9 + j]) {
                    return false
                }
            }
        }
        return true
    }

    fun copySudo(sudo: IntArray): IntArray {
        val newSudo = IntArray(81)
        for (i in sudo.indices) {
            newSudo[i] = sudo[i]
        }
        return newSudo
    }

    // 检查结果
    fun checkResult(sudo: IntArray): Boolean {
        val remainingNumbers = TreeSet<Int>()
        for (n in 1..9) {
            remainingNumbers.add(n)
        }
        for (i in 0..8) {
            val rowRemainNumbers = TreeSet(remainingNumbers)
            val columnRemainNumbers = TreeSet(remainingNumbers)
            val gridRemainNumbers = TreeSet(remainingNumbers)
            for (j in 0..8) {
                if (!rowRemainNumbers.remove(sudo[i * 9 + j])) {
                    return false
                }
                if (!columnRemainNumbers.remove(sudo[j * 9 + i])) {
                    return false
                }
                if (!gridRemainNumbers.remove(sudo[(i / 3 * 3 + j / 3) * 9 + i % 3 * 3 + j % 3])) {
                    return false
                }
            }
        }
        return true
    }


}