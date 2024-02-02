package com.chart.ganttchart.model

data class Task(
    val title: String,
    val startDate: String,
    val endDate: String,
    var progress:Int=0,
    val status: String? = null,
    val statusDate: String? = null,
    val percentageComplete: String? = null,
    var uuid: String? = null,
    var startIndex:Int=0,
    var endIndex:Int=0
)
