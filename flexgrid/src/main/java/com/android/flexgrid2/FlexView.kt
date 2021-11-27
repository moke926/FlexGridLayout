package com.android.flexgrid2

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class FlexView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr), FlexChild {

    private var mStartCoordinate = Pair(0, 0)
    private var mEndCoordinate = Pair(0, 0)

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
}