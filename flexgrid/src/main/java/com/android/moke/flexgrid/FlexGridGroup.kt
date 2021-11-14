package com.android.moke.flexgrid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kotlin.math.max
import kotlin.math.roundToInt

class FlexGridGroup
@JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var mStandardUnitSize = 0

    var mHorizontalGap = 0
    var mVerticalGap = 0

    init {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxYUnits = 0
        var maxXUnits = 0
        for(index in 0..childCount){
            val child = getChildAt(index)
            if(child.visibility != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                (child as? FlexChild)?.also {
                    maxXUnits = max(maxXUnits, it.getEndCoordinate().second)
                    maxYUnits = max(maxYUnits, it.getEndCoordinate().first)
                }
            }
        }

        if(maxXUnits > 0 && maxYUnits > 0){
            val containerWidth = MeasureSpec.getSize(widthMeasureSpec)
            val widthMode = MeasureSpec.getMode(widthMeasureSpec)
            when(widthMode){
                MeasureSpec.EXACTLY ->{
                    mStandardUnitSize = (containerWidth.toFloat() / maxXUnits).roundToInt()
                }
            }
        }
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        var maxHeightUnits = 0
        var maxWidthUnits = 0
        for(index in 0..childCount){
            val child = getChildAt(index) as? FlexChild
            child?.also {
                val widthUnits = it.getWidthUnits()
                val heightUnits = it.getHeightUnits()
                maxHeightUnits = max(maxHeightUnits, heightUnits)
                maxWidthUnits = max(maxWidthUnits, widthUnits)
            }
        }
    }


    fun injectView(child: FlexChild){
        when(child){
            is View -> {
                addView(child)
            }
            else -> {
                //use default Flex GridView
                val view = FlexView(context)
                addView(view)
            }
        }
    }



}