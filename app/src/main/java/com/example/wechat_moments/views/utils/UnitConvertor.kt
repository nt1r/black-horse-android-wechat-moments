package com.example.wechat_moments.views.utils

import android.content.Context
import android.util.TypedValue

class UnitConvertor {
    companion object {
        fun convertDpToPx(context: Context, dp: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
        }
    }
}