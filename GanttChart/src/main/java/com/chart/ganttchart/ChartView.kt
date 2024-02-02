package com.chart.ganttchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.chart.ganttchart.model.Project
import com.chart.ganttchart.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs


class ChartView : FrameLayout {

    private var project : Project?=null
    private var tasks = ArrayList<Task>()
    private var days =  listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")


    private var dayList =ArrayList<DayData>()
    private var projectSize = 0
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var maxXValue=0f
    private var maxYValue=0f
    private var velocityTracker:VelocityTracker?=null

    private lateinit var fling: Fling
    private var minimumVelocity = 0
    private var maximumVelocity = 0
    private var touchSlop = 0

    private lateinit var textPaint: TextPaint
    private lateinit var calendarTextPaint: TextPaint
    private lateinit var taskTextPaint: TextPaint
    private lateinit var linePaint: TextPaint
    private lateinit var rectPaint: TextPaint
    private lateinit var progressPaint: TextPaint
    private lateinit var rowPaint: Paint
    private var textWidth: Float = 80.toPx
    private var textHeight: Float = 0f
    private var taskWidth: Float = 0f

    private var blackColor = ContextCompat.getColor(context,R.color.black)
    private var whiteColor = ContextCompat.getColor(context,R.color.white)
    private var greyColor = ContextCompat.getColor(context,R.color.grey)
    private var darkGreyColor = ContextCompat.getColor(context,R.color.grey2)
    private var taskColor = ContextCompat.getColor(context,R.color.task)
    private var taskProgressColor = ContextCompat.getColor(context,R.color.task_progress)


    fun updateData(project : Project, tasks : ArrayList<Task>){
        this.project=project
        this.tasks=tasks
        updateTask()
        textPaint.let {
            it.color = blackColor
            textHeight = it.fontMetrics.bottom

            taskWidth = it.measureText(tasks[0].title)
        }
        marginX=taskWidth+columnHorizontalPadding
        projectSize=getDays()

        updateValues()
        getDayList()
        invalidate()
    }

    private fun updateValues(){
        maxXValue= ((textWidth+columnHorizontalPadding)*projectSize)-screenWidth
        maxYValue=(((textHeight*4)+rowVerticalPadding)*(tasks.size+2))-screenHeight //+2 here bcz calendar take height of two rows
        if ((((textHeight*4)+rowVerticalPadding)*(tasks.size+2))<screenHeight){     // same here
            maxYValue=boxHeight
        }
    }

    private fun updateTask(){
        val date1 = dateFormat.parse(project!!.startDate)

        tasks.forEach {
            it.startIndex=TimeUnit.MILLISECONDS.toDays(dateFormat.parse(it.startDate)!!.time-date1!!.time).toInt()
            it.endIndex=TimeUnit.MILLISECONDS.toDays(dateFormat.parse(it.endDate)!!.time-date1.time).toInt()
        }
    }

    data class DayData(
        var date:String,
        var dayIndex:Int,
        var dayName:String,
    )

    private fun getDayList(){
        val startDate = dateFormat.parse(project!!.startDate)
        startDate?.time
        dayList.clear()
        Calendar.getInstance().apply {
            timeInMillis=startDate!!.time

            for (i in 0 until projectSize){
                dayList.add(DayData(dateFormat.format(time),get(Calendar.DAY_OF_WEEK),days[get(Calendar.DAY_OF_WEEK)-1]) )
                add(Calendar.DATE, 1)
            }
        }
    }

    private fun initRotation(){
        val params= LayoutParams(44.toPx.toInt(),44.toPx.toInt())
        params.topMargin=5.toPx.toInt()
        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.ic_rotation)
        imageView.setPadding(10.toPx.toInt())
        addView(imageView,params)
        var isPortrait = true
        imageView.setOnClickListener {
            marginX=taskWidthPadding
            marginY=0f
            if (isPortrait){
                rotation = 90F
                val lp = layoutParams
                lp.height = screenWidth.toInt()
                lp.width = screenHeight.toInt()
            }else{
                rotation = 0F
                val lp = layoutParams
                lp.height = screenWidth.toInt()
                lp.width = screenHeight.toInt()
            }
            isPortrait = !isPortrait
            requestLayout()
        }
    }

    private fun getDays():Int{
        val startDate = dateFormat.parse(project!!.startDate)
        val endDate = dateFormat.parse(project!!.endDate)

        val diff: Long = (endDate?.time?:0L) - (startDate?.time?:0L)
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return (hours / 24).toInt()+1

    }

    private var _exampleDimension: Float = 0f
    private var _columnHorizontalPadding: Float = 0f
    private var _rowVerticalPadding: Float = 0f


    /**
     * In the example view, this dimension is the font size.
     */
    private var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
            invalidateTextPaintAndMeasurements()
        }

    private var columnHorizontalPadding: Float
        get() = _columnHorizontalPadding
        set(value) {
            _columnHorizontalPadding = value
            invalidateTextPaintAndMeasurements()
        }

    private var rowVerticalPadding: Float
        get() = _rowVerticalPadding
        set(value) {
            _rowVerticalPadding = value
            invalidateTextPaintAndMeasurements()
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ChartView, defStyle, 0
        )

        fling= Fling(context,this)
        val configuration= ViewConfiguration.get(context)
        touchSlop = configuration.scaledTouchSlop
        minimumVelocity = configuration.scaledMinimumFlingVelocity
        maximumVelocity = configuration.scaledMaximumFlingVelocity

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _exampleDimension = a.getDimension(
            R.styleable.ChartView_exampleDimension,
            exampleDimension
        )

        _columnHorizontalPadding = a.getDimension(
            R.styleable.ChartView_columnHorizontalPadding,
            columnHorizontalPadding
        )

        _rowVerticalPadding = a.getDimension(
            R.styleable.ChartView_rowVerticalPadding,
            rowVerticalPadding
        )

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
            textSize = 15.toPx
            color = blackColor
        }
        taskTextPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
            color=whiteColor
            textSize = 13.toPx
        }

        calendarTextPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
            color=taskProgressColor
            textSize = 13.toPx
        }

        linePaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            color=ContextCompat.getColor(context, R.color.black2)
            style= Paint.Style.STROKE
            strokeWidth = (0.5f).toPx
        }

        rectPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            color=taskColor
            style= Paint.Style.FILL
        }
        progressPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            color=taskProgressColor
            style= Paint.Style.FILL
        }

        rowPaint = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            color=ContextCompat.getColor(context, R.color.white)
            style= Paint.Style.FILL
        }

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
        initRotation()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            textHeight = it.fontMetrics.bottom
        }
    }

    private var screenWidth=0f
    private var screenHeight=0f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        screenWidth=w.toFloat()
        screenHeight=h.toFloat()
        updateValues()
    }
    private var calendarHeight=0f
    private var rowHeight=0f
    private var boxHeight=0f
    private var taskWidthPadding=0f

    private var taskStartX=0f
    private var taskEndX=0f
    private var progressEndX=0f
    private var progressPadding= 2.toPx
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (project==null) return
        taskWidthPadding=taskWidth+columnHorizontalPadding
        boxHeight=(textHeight*4)
        rowHeight=(textHeight*4)+rowVerticalPadding
        calendarHeight=rowHeight*2
        for (i in tasks.indices){
            (calendarHeight+(rowHeight*i)+marginY).let {
                rowPaint.color = if (i%2==0) whiteColor else greyColor
                canvas.drawRect(0f,it,width.toFloat(),it+rowHeight,rowPaint)
            }
        }
        for (i in 0 until projectSize){
            (((textWidth+columnHorizontalPadding)*i)+marginX).let {sX->
                canvas.drawLine(sX,if (dayList[i].dayIndex==2) 0f else rowHeight,sX,height.toFloat(),linePaint)
            }
        }
        //draw task
        for (i in tasks.indices){
            (calendarHeight+(rowHeight*i)+marginY).let {
                progressPadding = if (tasks[i].progress==0) 0f else 2.toPx
                taskStartX= (tasks[i].startIndex*(textWidth+columnHorizontalPadding))+marginX
                taskEndX= ((tasks[i].endIndex+1)*(textWidth+columnHorizontalPadding))+marginX
                progressEndX =taskStartX+(((taskEndX-taskStartX)*tasks[i].progress)/100)
                canvas.drawRoundRect(taskStartX,it+(rowVerticalPadding/4),taskEndX,it+rowHeight-(rowVerticalPadding/4),5.toPx,5.toPx,rectPaint)

                canvas.drawRoundRect(taskStartX+progressPadding,it+(rowVerticalPadding/4)+progressPadding,progressEndX-progressPadding,it+rowHeight-(rowVerticalPadding/4)-progressPadding,4.toPx,4.toPx,progressPaint)

                canvas.drawText(
                    tasks[i].title,
                    taskStartX+(columnHorizontalPadding/2),
                    it+(rowHeight/2)+(textHeight) ,
                    taskTextPaint
                )
            }
        }

        //draw top calendar view
        canvas.drawRect(0f,0f,width.toFloat(),calendarHeight,rowPaint.apply { color=darkGreyColor })

        for (i in 0 until projectSize){
            (((textWidth+columnHorizontalPadding)*i)+marginX).let {sX->
                if (i==0 || dayList[i].dayIndex==2){
                    canvas.drawText(
                        dayList[i].date/*dayList[i].date*/,
                        sX+(columnHorizontalPadding/2),
                        paddingTop+(textHeight*3),
                        calendarTextPaint
                    )
                }
                canvas.drawText(
                    dayList[i].dayName/*dayList[i].date*/,
                    sX+(columnHorizontalPadding/2),
                    paddingTop+rowHeight+(textHeight*3),
                    calendarTextPaint
                )
                canvas.drawLine(sX,if (dayList[i].dayIndex==2) 0f else rowHeight,sX,calendarHeight,linePaint)
            }
        }
        canvas.drawLine(0f,rowHeight,width.toFloat(),rowHeight,linePaint)
        canvas.drawLine(0f,calendarHeight,width.toFloat(),calendarHeight,linePaint)

        //draw left side row
        canvas.drawRect(0f,0f,taskWidthPadding,height.toFloat(),rowPaint.apply { color=whiteColor })
        for (i in tasks.indices){
            (calendarHeight+(rowHeight*i)+marginY).let {
                rowPaint.color = if (i%2==0) whiteColor else greyColor
                canvas.drawRect(0f,it,taskWidthPadding,it+rowHeight,rowPaint)
                canvas.drawText(
                    tasks[i].title,
                    0f,
                    it+(rowHeight/2)+(textHeight) ,
                    textPaint
                )
            }
        }
        canvas.drawRect(0f,0f,taskWidthPadding,calendarHeight,rowPaint.apply { color=whiteColor })
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        super.dispatchTouchEvent(event)
        if (velocityTracker==null) velocityTracker = VelocityTracker.obtain()
        velocityTracker?.addMovement(event)
        when(event?.actionMasked){
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN->touchDown(event)
            MotionEvent.ACTION_MOVE->touchMove(event)
            MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL-> touchUp(event)
        }
        return true
    }
    private var xPos=0f
    private var yPos=0f

    private var initX=0f
    private var initY=0f
    private var diffX=0f
    private var diffY=0f

    private fun touchDown(event: MotionEvent){
        if (!fling.isFinished) { // If scrolling, then stop now
            fling.forceFinished()
        }
        xPos=event.x
        yPos=event.y
        initX=xPos
        initY=yPos
        initialMarginX=marginX
        initialMarginY=marginY
    }

    private fun touchMove(event: MotionEvent){
        xPos=event.x
        yPos=event.y
        diffX=initX-xPos
        diffY=initY-yPos
        updateXPosition()
    }

    private fun touchUp(event: MotionEvent){
        xPos=event.x
        yPos=event.y


        velocityTracker?.computeCurrentVelocity(1000, maximumVelocity.toFloat())


        val velocityX = velocityTracker?.xVelocity?.toInt()?:0
        val velocityY = velocityTracker?.yVelocity?.toInt()?:0

        if (abs(velocityX) > minimumVelocity || abs(velocityY) > minimumVelocity) {
            fling.start(
                getActualScrollX(),
                getActualScrollY(),
                -velocityX,
                -velocityY,
                getMaxScrollX(),
                getMaxScrollY()
            )
        } else {
            velocityTracker?.recycle()
            velocityTracker=null
        }
    }

    private fun getActualScrollX():Int{
       return scrollX
    }
    private fun getActualScrollY():Int{
        return scrollY
    }
    private fun getMaxScrollX():Int{
        return maxXValue.toInt()
    }
    private fun getMaxScrollY():Int{
        return maxYValue.toInt()
    }
    override fun computeVerticalScrollRange(): Int {
        return height
    }

    override fun computeHorizontalScrollExtent(): Int {
        return width
    }

    private var marginX=0f
    private var marginY=0f
    private var initialMarginX=0f
    private var initialMarginY=0f
    private fun updateXPosition(){
        marginX=initialMarginX-diffX
        marginY=initialMarginY-diffY

        if (marginX>taskWidthPadding)marginX=taskWidthPadding
        if (marginX<(-maxXValue)){
            marginX=-maxXValue
        }

        if (marginY>0)marginY=0f
        if (marginY<(-maxYValue)){
            marginY=-maxYValue
        }
        Log.e("updateXPosition", "updateXPosition: $marginY, ${-maxYValue}")

        invalidate()
    }
    private var scrollX = 0
    private var scrollY = 0
    override fun scrollBy(x: Int, y: Int) {
        scrollX = x
        scrollY = y
        if (scrollX!=0){
            marginX +=scrollX
            if (marginX > taskWidthPadding) {
                marginX = taskWidthPadding
              //  fling.forceFinished()
            }
            if (marginX < (-maxXValue)) {
                marginX = -maxXValue
                //fling.forceFinished()
            }
            invalidate()
        }
        if (scrollY!=0){
            marginY += scrollY
            if (marginY>0)marginY=0f
            if (marginY<(-maxYValue)) marginY=-maxYValue
            invalidate()
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        scrollX += x
        scrollY += y
    }
}