package com.android.flexgrid2.constants

import androidx.annotation.IntDef

/**
 * @author moke926
 * @date 2021/12/5
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(TouchMode.TOUCH_MODE_REST, TouchMode.TOUCH_MODE_DONE_WAITING, TouchMode.TOUCH_MODE_DOWN, TouchMode.TOUCH_MODE_TAP)
annotation class TouchMode(){
    companion object{
        const val TOUCH_MODE_REST = 0
        const val TOUCH_MODE_DOWN = 1
        const val TOUCH_MODE_DONE_WAITING = 2
        const val TOUCH_MODE_TAP = 3
    }
}
