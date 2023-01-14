package com.miaogue.sudoku.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.miaogue.sudoku.R
import kotlin.math.abs
import kotlin.math.floor

class SudokuView : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.SudokuView)

        gridLineWidth = typeArray.getFloat(R.styleable.SudokuView_gridLineWidth, 5f)
        gridLineColor =
            typeArray.getColor(R.styleable.SudokuView_gridLineColor, Color.parseColor("#91CEFD"))
        unitLineWidth = typeArray.getFloat(R.styleable.SudokuView_unitLineWidth, 2f)
        unitLineColor =
            typeArray.getColor(R.styleable.SudokuView_unitLineColor, Color.parseColor("#D7E5F2"))
        textSize = typeArray.getFloat(R.styleable.SudokuView_numberTextSize, 50f)
        textColor =
            typeArray.getColor(R.styleable.SudokuView_numberTextColor, Color.parseColor("#8A8F95"))
        selectedBackgroundColor = typeArray.getColor(
            R.styleable.SudokuView_selectedBackgroundColor,
            Color.parseColor("#4C99C7")
        )
        hintBackgroundColor = typeArray.getColor(
            R.styleable.SudokuView_numberHintBackground,
            Color.parseColor("#89D0FA")
        )
        numberBackgroundColor = typeArray.getColor(
            R.styleable.SudokuView_numberBackground,
            Color.parseColor("#DEE3E9")
        )
        // 释放资源
        typeArray.recycle()
    }

    /**
     * 计算控件大小
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 获取控件的宽高
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
//        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        // 如果宽度大于高度,取高度   否则取宽度
        matrixWidth = widthSize - gridLineWidth * 2
        matrixStartX = gridLineWidth
        matrixStartY = gridLineWidth
        matrixEndX = matrixWidth + gridLineWidth
        matrixEndY = matrixWidth + gridLineWidth
        unitWidth = matrixWidth / 9
        gridWidth = matrixWidth / 3
        numberBackgroundRadius = unitWidth / 2 - unitMargin
        // 设置控件的宽高
        setMeasuredDimension(widthSize, widthSize)
    }


    // 矩阵宽度 、 坐标.
    private var matrixWidth: Float = 100f
    private var matrixStartX: Float = 0f
    private var matrixStartY: Float = 0f
    private var matrixEndX: Float = 100f
    private var matrixEndY: Float = 100f

    // 计算单元格和宫格的宽度
    private var unitWidth = matrixWidth / 9
    private var gridWidth = matrixWidth / 3

    // 数据
    private var data = mutableListOf<Int>()
    private var baseData = mutableListOf<Int>()

    // 是否结束
    private var isComplete = false

    // 选中数字的索引
    private var selectedIndex = -1

    // 矩阵外边框 - 是否显示
    private var isMatrixLine: Boolean = false

    // 九宫格 - 线宽
    private var gridLineWidth: Float = 5f

    // 九宫格 - 线颜色
    private var gridLineColor = Color.parseColor("#91CEFD")

    // 单元格 - 线宽
    private var unitLineWidth: Float = 2f

    // 单元格 - 线颜色
    private var unitLineColor = Color.parseColor("#D7E5F2")

    // 单元格 - 边距
    private var unitMargin = 10f

    // 数字背景圆半径
    private var numberBackgroundRadius = unitWidth / 2 - unitMargin

    // 字体大小
    private var textSize = 50F
    private var textColor = Color.parseColor("#8A8F95")
    private var selectedTextColor = Color.parseColor("#FFFFFF")

    // 选中背景颜色
    private var selectedBackgroundColor = Color.parseColor("#4C99C7")

    // 提示背景颜色
    private var hintBackgroundColor = Color.parseColor("#89D0FA")

    // 数字背景
    private var numberBackgroundColor = Color.parseColor("#DEE3E9")

    // 画笔
    private var gridPaint = Paint()
    private var unitPaint = Paint()
    private var selectedPaint = Paint()
    private var hintPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()
    private var selectedTextPaint = Paint()

    @SuppressLint("Range", "DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 检查数独
        inspect()
        // 初始化画笔
        initPaint()


        // 矩阵
        if (isMatrixLine) {
            canvas.drawRect(matrixStartX, matrixStartY, matrixEndX, matrixEndY, gridPaint)
        }

        // 画出单个小九宫格
        val gridLine = FloatArray(16)
        for (i in 1..2) {
            val k = gridWidth * i + gridLineWidth
            val j = matrixWidth + gridLineWidth
            gridLine[0 + (i - 1) * 8] = gridLineWidth
            gridLine[1 + (i - 1) * 8] = k
            gridLine[2 + (i - 1) * 8] = j
            gridLine[3 + (i - 1) * 8] = k
            gridLine[4 + (i - 1) * 8] = k
            gridLine[5 + (i - 1) * 8] = gridLineWidth
            gridLine[6 + (i - 1) * 8] = k
            gridLine[7 + (i - 1) * 8] = j
        }
        canvas.drawLines(gridLine, gridPaint)

        // 绘制单元格线
        for (i in 1..8) {
            if (i % 3 != 0) {
                val j = unitWidth * i + gridLineWidth
                val unitLine = FloatArray(108)
                for (index in 0..8) {
                    unitLine[0 + index * 8] = gridLineWidth + unitMargin + index * unitWidth
                    unitLine[1 + index * 8] = j
                    unitLine[2 + index * 8] =
                        gridLineWidth + unitWidth - unitMargin * 2 + index * unitWidth
                    unitLine[3 + index * 8] = j
                    unitLine[4 + index * 8] = j
                    unitLine[5 + index * 8] = gridLineWidth + unitMargin + index * unitWidth
                    unitLine[6 + index * 8] = j
                    unitLine[7 + index * 8] =
                        gridLineWidth + unitWidth - unitMargin * 2 + index * unitWidth
                }
                canvas.drawLines(unitLine, unitPaint)
            }
        }

        // 判断是否选中单元格
        if (selectedIndex in (0..80)) {
            val unitXIndex = selectedIndex % 9
            val unitYIndex = selectedIndex / 9
            // 绘制选中单元格
            if (unitXIndex <= 8 && unitYIndex <= 8) {
                val selectedX = unitXIndex * unitWidth + gridLineWidth + unitWidth / 2
                val selectedY = unitYIndex * unitWidth + gridLineWidth + unitWidth / 2
                // 绘制选中区域
                canvas.drawCircle(selectedX, selectedY, numberBackgroundRadius, selectedPaint)
            }
        }

        val fontMetricsInt = textPaint.fontMetricsInt
        val fontHeight = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom
        // 绘制文本和错误提示区域
        for (i in data.indices) {
            // 计算文本绘制偏移
            val offsetWidth = i % 9 * unitWidth
            val offsetHeight = i / 9 * unitWidth
            val x = gridLineWidth + unitWidth / 2 + offsetWidth
            val y = gridLineWidth + unitWidth / 2 + fontHeight + offsetHeight
            val text = if (data[i] == 0) "" else data[i]
            // 绘制错误提示区域
//            if (errorData[i] != 0) {
//                val unitXIndex = floor(((x - gridLineWidth) / unitWidth).toDouble()).toInt()
//                val unitYIndex = floor(((y - gridLineWidth) / unitWidth).toDouble()).toInt()
//                canvas.drawRect(
//                    unitXIndex * unitWidth + gridLineWidth,
//                    unitYIndex * unitWidth + gridLineWidth,
//                    (unitXIndex + 1) * unitWidth + gridLineWidth,
//                    (unitYIndex + 1) * unitWidth + gridLineWidth,
//                    textPaint
//                )
//            }
            if (baseData[i] != 0 && i != selectedIndex) {
                canvas.drawCircle(
                    x,
                    y - fontHeight,
                    unitWidth / 2 - unitMargin,
                    textBackgroundPaint
                )
            }
            // 绘制文本
            if ((i == selectedIndex || (selectedIndex in 0..80 && data[i] == data[selectedIndex])) && data[i] != 0) {
                if (i != selectedIndex) {
                    canvas.drawCircle(x, y - fontHeight, unitWidth / 2 - unitMargin, hintPaint)
                }
                canvas.drawText(text.toString(), x, y, selectedTextPaint)
            } else {
                canvas.drawText(text.toString(), x, y, textPaint)
            }
        }
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        gridPaint.color = gridLineColor
        gridPaint.isAntiAlias = true
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeWidth = gridLineWidth

        unitPaint.color = unitLineColor
        unitPaint.isAntiAlias = true
        unitPaint.style = Paint.Style.STROKE
        unitPaint.strokeWidth = unitLineWidth

        selectedPaint.color = selectedBackgroundColor
        selectedPaint.isAntiAlias = true
        selectedPaint.style = Paint.Style.FILL

        hintPaint.color = hintBackgroundColor
        hintPaint.isAntiAlias = true
        hintPaint.style = Paint.Style.FILL

        textBackgroundPaint.color = numberBackgroundColor
        textBackgroundPaint.isAntiAlias = true
        textBackgroundPaint.style = Paint.Style.FILL

        textPaint.color = textColor
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isAntiAlias = true

        selectedTextPaint.color = selectedTextColor
        selectedTextPaint.textSize = textSize
        selectedTextPaint.textAlign = Paint.Align.CENTER
        selectedTextPaint.isAntiAlias = true
    }

    // 初始化触摸屏幕的坐标
    private var lastX = 0
    private var lastY = 0
    private var downX = 0
    private var downY = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = x
                downY = y
            }
            MotionEvent.ACTION_UP -> {
                if (!isComplete && (abs(x - downX) <= 10 && abs(y - downY) <= 10)) {
                    lastX = x
                    lastY = y

                    // 计算选中的索引 (如果选中同一个则取消选中)
                    val unitXIndex = floor(((lastX - gridLineWidth) / unitWidth).toDouble()).toInt()
                    val unitYIndex = floor(((lastY - gridLineWidth) / unitWidth).toDouble()).toInt()
                    val newSelectedIndex = unitYIndex * 9 + unitXIndex
                    selectedIndex =
                        if (selectedIndex > 0 && newSelectedIndex == selectedIndex) -1 else newSelectedIndex

                    invalidate()
                } else {
                    lastX = 0
                    lastY = 0
                }
            }
        }
        //返回true 表名该处理方法已经处理该事件
        return true
    }

    /**
     * 检查数独，生成错误数组
     */
    private fun inspect() {
        if (data.toIntArray().isNotEmpty()) {
            val complete = Algorithm().checkResult(data.toIntArray())
            if (this::onCallListener.isInitialized) {
                onCallListener.onResult(data, baseData, complete)
                isComplete = complete
            }
            if (complete && selectedIndex >= 0) {
                selectedIndex = -1
                invalidate()
            }
        }
    }

    /**
     * 初始化数独
     * 81位
     */
    fun init(data: IntArray) {
        for (i in data.iterator()) {
            this.data.add(i)
            this.baseData.add(i)
        }
        invalidate()
    }

    fun init(data: List<Int>, baseData: List<Int>) {
        for (i in data.iterator()) {
            this.data.add(i)
        }
        for (i in baseData.iterator()) {
            this.baseData.add(i)
        }
        invalidate()
    }

    /**
     * 生成算法
     * 一级  50 - 57
     * 二级  40 - 44
     * 三级  30 - 36
     * 四级  23 - 28
     */


    /**
     * 设置当前选中的数字
     */
    fun setSelectNumber(number: Int) {
        if (this.selectedIndex in 0..80) {
            if (this.baseData[this.selectedIndex] == 0) {
                if (number in 0..9) {
                    this.data[this.selectedIndex] = number
                    invalidate()
                } else {
                    Log.e("SudokuView", "setSelectNumber -> 方法参数不在规定范围内")
                }
            } else {
                Log.e("SudokuView", "setSelectNumber -> 当前选中位置是基础数字，不能修改")
            }
        }
    }

    interface OnCallListener {
        fun onResult(data: List<Int>, baseData: List<Int>, isComplete: Boolean)
    }

    private lateinit var onCallListener: OnCallListener

    fun setOnCallListener(onCallListener: OnCallListener) {
        this.onCallListener = onCallListener
    }
}