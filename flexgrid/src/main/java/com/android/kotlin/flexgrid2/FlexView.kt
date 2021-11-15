package com.android.kotlin.flexgrid2

import android.content.Context
import android.util.AttributeSet
import android.view.View

class FlexView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    View(context, attrs, defStyleAttr, defStyleRes), FlexChild {

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