package com.android.flexgrid2

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import kotlin.math.max
import kotlin.math.roundToInt

open class FlexGridGroup
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    companion object{
        const val TAG = "FlexGridGroup"
    }

    private var mStandardUnitSize: Int
    private var mHorizontalGap = 0
    private var mVerticalGap = 0

    private var mAdapter: FlexAdapter<*>? = null

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.FlexGridGroup, defStyleAttr, defStyleRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveAttributeDataForStyleable(context, R.styleable.FlexGridGroup, attrs, typeArray, defStyleAttr, defStyleRes)
        }
        mHorizontalGap = typeArray.getDimensionPixelOffset(R.styleable.FlexGridGroup_horizontalGap, mHorizontalGap)
        mVerticalGap = typeArray.getDimensionPixelOffset(R.styleable.FlexGridGroup_verticalGap, mVerticalGap)
        mStandardUnitSize = typeArray.getDimensionPixelSize(R.styleable.FlexGridGroup_standUnitSize, 0)
        typeArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxYUnits = 0
        var maxXUnits = 0

        for(index in 0 until childCount){
            val child = getChildAt(index)
            if(child.visibility != GONE) {
                (child as? FlexChild)?.also {
                    maxXUnits = max(maxXUnits, it.getEndCoordinate().second + 1)
                    maxYUnits = max(maxYUnits, it.getEndCoordinate().first + 1)
                }
            }
        }

        if(maxXUnits > 0 && maxYUnits > 0){
            val containerWidth = MeasureSpec.getSize(widthMeasureSpec)
            var widthType = MeasureSpec.getMode(widthMeasureSpec)
            if(widthType == MeasureSpec.AT_MOST && mStandardUnitSize <= 0){
                widthType = MeasureSpec.EXACTLY
            }
            when(widthType){
                MeasureSpec.EXACTLY, MeasureSpec.UNSPECIFIED ->{
                    val totalHorizontalGap = (maxXUnits - 1) * mHorizontalGap
                    val spaceWithoutGap = containerWidth - paddingStart - paddingEnd - totalHorizontalGap
                    mStandardUnitSize = (spaceWithoutGap.toFloat() / maxXUnits).roundToInt()
                    if(mStandardUnitSize > 0){
                        for(index in 0 until childCount){
                            val child = getChildAt(index)
                            val widthUnits = (child as? FlexChild)?.getWidthUnits()?:0
                            val heightUnits = (child as? FlexChild)?.getHeightUnits()?:0
                            if(child.visibility != GONE) {
                                val width = mStandardUnitSize * widthUnits + (widthUnits - 1) * mHorizontalGap
                                val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                                val height = mStandardUnitSize * heightUnits + (heightUnits - 1) * mVerticalGap
                                val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
                                child.measure(widthSpec, heightSpec)
                            }
                        }
                    }
                    val totalHeight = mStandardUnitSize * maxYUnits + mVerticalGap * (maxYUnits - 1)
                    setMeasuredDimension(containerWidth, totalHeight)
                }
                MeasureSpec.AT_MOST -> {
                    for(index in 0 until childCount){
                        val child = getChildAt(index)
                        val widthUnits = (child as? FlexChild)?.getWidthUnits()?:0
                        val heightUnits = (child as? FlexChild)?.getHeightUnits()?:0
                        if(child.visibility != GONE) {
                            val width = mStandardUnitSize * widthUnits + (widthUnits - 1) * mHorizontalGap
                            val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                            val height = mStandardUnitSize * heightUnits + (heightUnits - 1) * mVerticalGap
                            val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
                            child.measure(widthSpec, heightSpec)
                        }
                    }
                    val totalHeight = mStandardUnitSize * maxYUnits + mVerticalGap * (maxYUnits - 1)
                    setMeasuredDimension(containerWidth, totalHeight)
                }
                else -> {
                    val width = max(ViewCompat.getMinimumWidth(this), paddingStart+paddingEnd)
                    val height = max(ViewCompat.getMinimumHeight(this), paddingTop+paddingBottom)
                    setMeasuredDimension(width, height)
                }
            }
        }
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        if(mStandardUnitSize <= 0) return
        for (index in 0 until childCount){
            val child = getChildAt(index)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            val startCoordinate = (child as? FlexChild)?.getStartCoordinate()
            startCoordinate?.also {
                val left = paddingLeft + it.second * (mStandardUnitSize + mHorizontalGap)
                val top = paddingTop + it.first * (mStandardUnitSize + mVerticalGap)
                val right = left + childWidth
                val bottom = top + childHeight
                child.layout(left, top, right, bottom)
            }
        }
    }

    fun setStandUnitSize(pxSize: Int){
        mStandardUnitSize = pxSize
    }

    fun addGrids(list: List<FlexChild>){
        removeAllViews()
        if (list.isNotEmpty()){
            list.forEachIndexed { _, flexChild ->
                injectView(flexChild)
            }
        }
    }

    private fun injectView(child: FlexChild){
        when(child){
            is View -> {
                addView(child)
            }
            else -> {
                val flexView = FlexView(context).apply {
                    setCoordinate(child.getStartCoordinate(), child.getEndCoordinate())
                }
                addView(flexView)
            }
        }
    }

    fun setAdapter(adapter: FlexAdapter<*>){
        mAdapter = adapter
    }

    fun getAdapter(): FlexAdapter<*>?{
        return mAdapter
    }

    fun bindAdapterData(){
        for (index in 0 until childCount){
            val child = getChildAt(index) as? FlexChild
            if(child != null) {
                mAdapter?.bindDataAndView(index, child)
            }
        }
    }

    abstract class FlexAdapter<D>{

        private var mList = mutableListOf<D>()
        fun submitData(group: View?, list: List<D>){
            if(group !is FlexGridGroup){
                Log.e(TAG, "groupView with this FlexAdapter is not FlexGridGroup!")
                return
            }
            mList.clear()
            mList.addAll(list)
            group.bindAdapterData()
        }

        fun bindDataAndView(position: Int, flexChild: FlexChild){
            val data = mList.getOrNull(position)
            if(data != null){
                bind(data, flexChild)
            }
        }

        abstract fun bind(data:D, flexChild: FlexChild)
    }
}






