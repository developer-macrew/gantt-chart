package com.ethan.ganttchartlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chart.ganttchart.ChartView
import com.chart.ganttchart.model.Project
import com.chart.ganttchart.model.Task

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val project = Project( "Test", "2024-01-21", "2024-02-20","1")
        val tasks = ArrayList<Task>()

        tasks.add(Task("InProgress", "2024-01-21", "2024-01-21",0, null, null, null,(-1).toString() ))
        tasks.add(Task("Task1", "2024-01-23", "2024-01-24",20,null, null, null,(-1).toString() ))
        tasks.add(Task("Task2", "2024-01-24", "2024-01-24",10,null, null, null,(-1).toString() ))
        tasks.add(Task("Task3", "2024-01-22", "2024-01-25",90,null, null, null,(-1).toString() ))
        tasks.add(Task("Task4", "2024-01-22", "2024-01-22",100,null, null, null,(-1).toString() ))
        tasks.add(Task("Task5", "2024-01-24", "2024-01-26",50,null, null, null,(-1).toString() ))
        tasks.add(Task("Task6", "2024-01-23", "2024-01-28",0,null, null, null,(-1).toString() ))
        tasks.add(Task("Task7", "2024-01-22", "2024-01-24",0,null, null, null,(-1).toString() ))
        tasks.add(Task("Task8", "2024-01-24", "2024-01-24",10,null, null, null,(-1).toString() ))
        tasks.add(Task("InProgress", "2024-01-21", "2024-01-21",100, null, null, null,(-1).toString() ))

        tasks.add(Task("Task9", "2024-01-22", "2024-01-24",80,null, null, null,(-1).toString() ))
        tasks.add(Task("Task10", "2024-01-23", "2024-01-24",100,null, null, null,(-1).toString() ))
        tasks.add(Task("Task11", "2024-01-22", "2024-01-28",90,null, null, null,(-1).toString() ))
        tasks.add(Task("InProgress", "2024-01-22", "2024-01-22",0, null, null, null,(-1).toString() ))
        tasks.add(Task("InProgress", "2024-01-21", "2024-01-21",70, null, null, null,(-1).toString() ))
        tasks.add(Task("InProgress", "2024-01-21", "2024-01-21",0, null, null, null,(-1).toString() ))
        tasks.add(Task("Task1", "2024-01-23", "2024-01-24",20,null, null, null,(-1).toString() ))
        tasks.add(Task("Task2", "2024-01-24", "2024-01-24",10,null, null, null,(-1).toString() ))
        tasks.add(Task("Task3", "2024-01-22", "2024-01-25",90,null, null, null,(-1).toString() ))
        tasks.add(Task("Task4", "2024-01-22", "2024-01-22",100,null, null, null,(-1).toString() ))
        tasks.add(Task("Task5", "2024-01-24", "2024-01-26",50,null, null, null,(-1).toString() ))
        tasks.add(Task("Task6", "2024-01-23", "2024-01-28",0,null, null, null,(-1).toString() ))
        tasks.add(Task("Task7", "2024-01-22", "2024-01-24",0,null, null, null,(-1).toString() ))
        tasks.add(Task("Task8", "2024-01-24", "2024-01-24",10,null, null, null,(-1).toString() ))
        tasks.add(Task("InProgress", "2024-01-21", "2024-01-21",100, null, null, null,(-1).toString() ))

        tasks.add(Task("Task9", "2024-01-22", "2024-01-24",80,null, null, null,(-1).toString() ))
        tasks.add(Task("Task10", "2024-01-23", "2024-01-24",100,null, null, null,(-1).toString() ))
        tasks.add(Task("Task11", "2024-01-22", "2024-01-28",90,null, null, null,(-1).toString() ))
        tasks.add(Task("InProgress", "2024-01-22", "2024-01-22",0, null, null, null,(-1).toString() ))
        tasks.add(Task("InProgress", "2024-01-21", "2024-01-21",70, null, null, null,(-1).toString() ))

        findViewById<ChartView>(R.id.chart).updateData(project,tasks)
    }
}