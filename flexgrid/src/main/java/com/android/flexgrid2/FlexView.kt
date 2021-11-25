package com.android.flexgrid2

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.roundToInt

class FlexView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr), FlexChild {

    private var mStartCoordinate = Pair(0, 0)
    private var mEndCoordinate = Pair(0, 0)

    init {
        scaleType = ScaleType.CENTER_CROP
    }
    override fun setCoordinate(startCoordinate: Pair<Int, Int>, endCoordinate: Pair<Int, Int>) {
        mStartCoordinate = startCoordinate
        mEndCoordinate = endCoordinate
    }

    override fun getStartCoordinate(): Pair<Int, Int> {
        return mStartCoordinate
    }

    override fun getEndCoordinate(): Pair<Int, Int> {
        return mEndCoordinate
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val ratio = getHeightUnits() / getWidthUnits().toFloat()
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (width * ratio).roundToInt()
        setMeasuredDimension(width, height)
    }
}