package com.android.flexgrid2

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.*
import androidx.core.view.ViewCompat
import com.android.flexgrid2.adapters.FlexAdapter
import com.android.flexgrid2.constants.TouchMode.Companion.TOUCH_MODE_DONE_WAITING
import com.android.flexgrid2.constants.TouchMode.Companion.TOUCH_MODE_DOWN
import com.android.flexgrid2.constants.TouchMode.Companion.TOUCH_MODE_REST
import com.android.flexgrid2.constants.TouchMode.Companion.TOUCH_MODE_TAP
import com.android.flexgrid2.listeners.OnItemClickListener
import com.android.flexgrid2.listeners.OnItemLongClickListener
import kotlin.math.max
import kotlin.math.roundToInt

open class FlexGridGroup
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    companion object{
        const val TAG = "FlexGridGroup"
        const val TAG_IMAGE = "image"
        const val INVALID_POSITION = -1
    }

    private var mStandardUnitSize: Int
    private var mHorizontalGap = 0
    private var mVerticalGap = 0

    private var mAdapter: FlexAdapter<*>? = null

    private var mDataChanged = false

    private var mMotionPosition: Int = INVALID_POSITION

    private var mTouchRect: Rect? = null

    private var mTouchMode = TOUCH_MODE_REST
    private var mMotionX: Int = -1
    private var mMotionY: Int = -1

    private val mPerformClick: PerformClick by lazy {
        PerformClick()
    }

    private val mPerformLongClick: PerformLongClick by lazy {
        PerformLongClick()
    }

    private var mTouchResetRequest: Runnable? = null

    private val mOnItemClickListeners: MutableList<OnItemClickListener> by lazy {
        mutableListOf()
    }

    private val mOnItemLongClickListeners: MutableList<OnItemLongClickListener> by lazy {
        mutableListOf()
    }

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.FlexGridGroup, defStyleAttr, defStyleRes)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveAttributeDataForStyleable(context, R.styleable.FlexGridGroup, attrs, typeArray, defStyleAttr, defStyleRes)
        }
        mHorizontalGap = typeArray.getDimensionPixelOffset(R.styleable.FlexGridGroup_horizontalGap, mHorizontalGap)
        mVerticalGap = typeArray.getDimensionPixelOffset(R.styleable.FlexGridGroup_verticalGap, mVerticalGap)
        mStandardUnitSize = typeArray.getDimensionPixelSize(R.styleable.FlexGridGroup_standUnitSize, 0)
        typeArray.recycle()

        isClickable = true
        isFocusableInTouchMode = true
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
            }
        }else {
            val width = max(ViewCompat.getMinimumWidth(this), paddingStart + paddingEnd)
            val height = max(ViewCompat.getMinimumHeight(this), paddingTop + paddingBottom)
            setMeasuredDimension(width, height)
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
        mDataChanged = false
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
                child.tag = TAG_IMAGE
                addView(child)
            }
            else -> {
                val flexView = FlexView(context).apply {
                    setCoordinate(child.getStartCoordinate(), child.getEndCoordinate())
                }
                flexView.tag = TAG_IMAGE
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

    fun addOnItemLongClickListener(listener: OnItemClickListener?){
        if(listener != null) {
            if(!mOnItemClickListeners.contains(listener)) {
                mOnItemClickListeners.add(listener)
            }
        }
    }

    fun addOnItemLongClickListener(listener: OnItemLongClickListener?){
        if(listener != null) {
            if(!mOnItemLongClickListeners.contains(listener)) {
                mOnItemLongClickListeners.add(listener)
            }
        }
    }

    fun bindAdapterData(){
        mDataChanged = true
        for (index in 0 until childCount){
            val child = getChildAt(index) as? FlexChild
            if(child != null) {
                mAdapter?.bindDataAndView(index, child)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?:return false

        if(!isEnabled){
            return isClickable || isLongClickable
        }

        when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                onTouchDown(event)
            }
            MotionEvent.ACTION_UP -> {
                onTouchUp(event)
            }
            MotionEvent.ACTION_CANCEL -> {
                onTouchCancel()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                val x = event.getX(index)
                val y = event.getY(index)

                mMotionX = x.toInt()
                mMotionY = y.toInt()

                val motionPosition = pointToPosition(mMotionX, mMotionY)
                if(motionPosition >= 0){
                    mMotionPosition = motionPosition
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val x = mMotionX
                val y = mMotionY
                val motionPosition = pointToPosition(x, y)
                if(motionPosition >= 0){
                    mMotionPosition = motionPosition
                }
            }
        }
        return true
    }


    private fun onTouchDown(event: MotionEvent){
        val x = event.x.toInt()
        val y = event.y.toInt()

        val motionPosition = pointToPosition(x, y)

        if(motionPosition >= 0){
            mTouchMode = TOUCH_MODE_DOWN
            val child = getChildAt(motionPosition)
            if (child != null && !child.hasExplicitFocusable()) {
                child.isPressed = true
                isPressed = true
                if(!mDataChanged) {
                    if (isLongClickable) {
                        postDelayed(
                            mPerformLongClick,
                            ViewConfiguration.getLongPressTimeout().toLong()
                        )
                    } else {
                        mTouchMode = TOUCH_MODE_DONE_WAITING
                    }
                }else{
                    mTouchMode = TOUCH_MODE_DONE_WAITING
                }
            }
        }

        mMotionX = x
        mMotionY = y
        mMotionPosition = motionPosition
    }

    private fun onTouchUp(event: MotionEvent){
        when(mTouchMode) {
            TOUCH_MODE_DOWN, TOUCH_MODE_TAP, TOUCH_MODE_DONE_WAITING -> {
                val motionPosition = mMotionPosition
                val child = getChildAt(motionPosition)
                if (child != null && !child.hasExplicitFocusable()) {
                    if(mTouchMode == TOUCH_MODE_DOWN || mTouchMode == TOUCH_MODE_TAP) {
                        removeCallbacks(mPerformLongClick)
                        if (!mDataChanged) {
                            mTouchMode = TOUCH_MODE_TAP
                            child.isPressed = true
                            isPressed = true
                            if (mTouchResetRequest != null) {
                                removeCallbacks(mTouchResetRequest)
                            }
                            mPerformClick.mClickMotionPosition = motionPosition
                            mTouchResetRequest = Runnable {
                                mTouchMode = TOUCH_MODE_REST
                                child.isPressed = false
                                isPressed = false
                                if (!mDataChanged && isAttachedToWindow) {
                                    mPerformClick.run()
                                }
                            }
                            postDelayed(
                                mTouchResetRequest,
                                ViewConfiguration.getPressedStateDuration().toLong()
                            )
                            return
                        }else{
                            mTouchMode = TOUCH_MODE_REST
                        }
                    }else if(!mDataChanged){
                        mPerformClick.run()
                    }
                }
                mTouchMode = TOUCH_MODE_REST
            }
        }
    }

    private fun onTouchCancel(){
        isPressed = false
        mTouchMode = TOUCH_MODE_REST
        val motionView = getChildAt(mMotionPosition)
        removeCallbacks(mPerformLongClick)
        if(motionView != null){
            motionView.isPressed = false
        }
    }

    private fun performItemCLick(index: Int, flexChild: FlexChild){
        if(mOnItemClickListeners.size > 0) {
            playSoundEffect(SoundEffectConstants.CLICK)
            mOnItemClickListeners.forEach {
                it.onChildClick(index, flexChild)
            }
        }
    }

    private fun performItemLongClick(index: Int, flexChild: FlexChild){
        if(mOnItemLongClickListeners.size > 0) {
            notifyItemLongClick(index, flexChild)
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    private fun notifyItemLongClick(index: Int, flexChild: FlexChild){
        mOnItemLongClickListeners.forEach {
            it.onChildLongClick(index, flexChild)
        }
    }

    private fun pointToPosition(x: Int, y: Int): Int{
        var rect = mTouchRect
        if(rect == null){
            rect = Rect()
        }

        for (index in 0 until childCount){
            val child = getChildAt(index)
            if(child.visibility == View.VISIBLE){
                child.getHitRect(rect)
                if(rect.contains(x, y)){
                    return index
                }
            }
        }

        return INVALID_POSITION
    }

    inner class PerformClick: Runnable{

        var mClickMotionPosition: Int = INVALID_POSITION
        override fun run() {
            if(mDataChanged) return
            val itemCount = childCount
            if(itemCount > 0
                && mClickMotionPosition < itemCount){
                val view = getChildAt(mClickMotionPosition) as? FlexChild
                if(view != null){
                    performItemCLick(mClickMotionPosition, view)
                }
            }
        }
    }

    inner class PerformLongClick: Runnable{
        override fun run() {
            if(mTouchMode == TOUCH_MODE_DOWN) {
                val motionPosition = mMotionPosition
                val child = getChildAt(motionPosition) as? FlexChild
                if(child != null) {
                    if (!mDataChanged) {
                        performItemLongClick(motionPosition, child)
                        isPressed = false
                        (child as View).isPressed = false
                        mTouchMode = TOUCH_MODE_REST
                    } else {
                        mTouchMode = TOUCH_MODE_DONE_WAITING
                    }
                }
            }
        }
    }
}






