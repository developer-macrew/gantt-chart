package com.chart.ganttchart

import android.content.res.Resources
import android.util.TypedValue

fun Int.toPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Resources.getSystem().displayMetrics)
val Float.toPx : Float  get() = Resources.getSystem().displayMetrics.density* this
val Int.toPx : Float  get() = Resources.getSystem().displayMetrics.density* this
fun Float.toPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this , Resources.getSystem().displayMetrics)