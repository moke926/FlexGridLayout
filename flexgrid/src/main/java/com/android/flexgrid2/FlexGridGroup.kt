package com.android.flexgrid2

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import kotlin.math.max
import kotlin.math.roundToInt

class FlexGridGroup<D>
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    companion object{
        const val TAG = "FlexGridGroup"
    }

    var mStandardUnitSize = 0

    var mHorizontalGap = 0
    var mVerticalGap = 0

    private var mCallBackList: ArrayList<OnDataBindCallBack<D>> = ArrayList()

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.FlexGridGroup, defStyleAttr, defStyleRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveAttributeDataForStyleable(context, R.styleable.FlexGridGroup, attrs, typeArray, defStyleAttr, defStyleRes)
        }
        mHorizontalGap = typeArray.getDimensionPixelOffset(R.styleable.FlexGridGroup_horizontalGap, mHorizontalGap)
        mVerticalGap = typeArray.getDimensionPixelOffset(R.styleable.FlexGridGroup_verticalGap, mVerticalGap)
        typeArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxYUnits = 0
        var maxXUnits = 0
        for(index in 0 until childCount){
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
            when(MeasureSpec.getMode(widthMeasureSpec)){
                MeasureSpec.EXACTLY ->{
                    val totalHorizontalGap = (maxXUnits - 1) * mHorizontalGap
                    val spaceWithoutGap = containerWidth - totalHorizontalGap
                    mStandardUnitSize = (spaceWithoutGap.toFloat() / maxXUnits).roundToInt()
                    if(mStandardUnitSize > 0){
                        for(index in 0 until childCount){
                            val child = getChildAt(index)
                            val widthUnits = (child as? FlexChild)?.getWidthUnits()?:0
                            if(child.visibility != GONE) {
                                val width = mStandardUnitSize * widthUnits + (widthUnits - 1) * mHorizontalGap
                                val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                                measureChild(child, widthSpec, heightMeasureSpec)
                            }
                        }
                    }
                    val totalHeight = mStandardUnitSize * maxYUnits + mHorizontalGap * (maxYUnits - 1)
                    setMeasuredDimension(containerWidth, totalHeight)
                }
                else -> {
                    Log.e(TAG, "width of FlexGridGroup should be an exact size")
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
                val left = it.second * (mStandardUnitSize + mHorizontalGap)
                val top = it.first * (mStandardUnitSize + mHorizontalGap)
                val right = left + childWidth
                val bottom = top + childHeight
                child.layout(left, top, right, bottom)
            }
        }
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


    private fun bindAdapterData(list: List<D>){
        for (index in 0 until childCount){
            val child = getChildAt(index) as? FlexChild
            val data = list.getOrNull(index)
            if(data != null && child != null) {
                mCallBackList.forEach {
                    it.onBind(data, child)
                }
            }
        }
    }

    fun addCallBack(@NonNull callBack: OnDataBindCallBack<D>){
        mCallBackList.add(callBack)
    }

    fun removeCallBack(@NonNull callBack: OnDataBindCallBack<D>){
        mCallBackList.remove(callBack)
    }

    fun recycle(){
        mCallBackList.clear()
    }

    fun submitData(list: List<D>){
        bindAdapterData(list)
    }
}

interface OnDataBindCallBack<T>{
    fun onBind(data: T, child: FlexChild)
}

