package com.chart.ganttchart

import android.content.Context
import android.widget.Scroller
import com.chart.ganttchart.ChartView


class Fling internal constructor(context: Context?, val view: ChartView) : Runnable {
    private val scroller: Scroller
    private var lastX = 0
    private var lastY = 0

    init {
        scroller = Scroller(context)
    }

    fun start(
        initX: Int,
        initY: Int,
        initialVelocityX: Int,
        initialVelocityY: Int,
        maxX: Int,
        maxY: Int
    ) {
        scroller.fling(initX, initY, initialVelocityX, initialVelocityY, -maxX, maxX, -maxY, maxY)
        lastX = initX
        lastY = initY
        view.post(this)
    }

    override fun run() {
        if (scroller.isFinished) {
            return
        }
        val more = scroller.computeScrollOffset()
        val x = scroller.currX
        val y = scroller.currY
        val diffX = lastX - x
        val diffY = lastY - y
        if (diffX != 0 || diffY != 0) {
            view.scrollBy(diffX, diffY)
            lastX = x
            lastY = y
        }
        if (more) {
            view.post(this)
        }
    }

    val isFinished: Boolean
        get() = scroller.isFinished

    fun forceFinished() {
        if (!scroller.isFinished) {
            scroller.forceFinished(true)
        }
    }
}
