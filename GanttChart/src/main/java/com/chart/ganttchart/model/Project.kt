package com.chart.ganttchart.model

data class Project(
    var name: String? = null,
    val startDate: String,
    val endDate: String,
    val uuid: String? = null,
)
