/*
 * @author Boris.A
 * @email boris.alsaev@gmail.com
 *
 */

package myapps.alsaev.com.layerprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class LayerProgressBar : View {
    private val DEFAULT_LAYER_COUNT = 3
    private val DEFAULT_LAYER_WIDTH = dpToPx(3f)
    private val DEFAULT_LAYER_GAP = dpToPx(4f)
    private val DEFAULT_LAYER_LENGTH = 250
    private val DEFAULT_OVAL_RADIUS = dpToPx(4f)
    private val ANIMATION_DURATION = 2000L
    private val DEFAULT_COLOR = android.R.color.black

    private var isRunning = false
    private val paint = Paint()
    private var ovalPaint = Paint()
    private var currentPosition = 0f
    private var currentPositions = arrayListOf<Float>()
    private var viewWidth = 0
    private var viewHeght = 0
    private var ovalRect = RectF(0f, 0f, 0f, 0f)
    private val layersRects = ArrayList<RectF>()
    private lateinit var animator: ValueAnimator

    // radius of center circle
    var ovalRadius = DEFAULT_OVAL_RADIUS

    // Width of lines
    var strokeWidth = dpToPx(2f)

    // Space between lines
    var layerGap = dpToPx(2f)

    // Count of lines
    var layerCount = DEFAULT_LAYER_COUNT

    // Length of lines (in degrees)
    var layerLength = DEFAULT_LAYER_LENGTH

    var animationDuration = ANIMATION_DURATION

    var isMultiThreading = true

    init {
        // Init paint for circle
        paint.color = resources.getColor(DEFAULT_COLOR)
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
    }

    constructor(context: Context) : super(context) {
        initFigures()
    }

    /**
     * Init figures rectangles for draw
     */
    private fun initFigures() {
        initLayersRects()
        initOvalRect()
    }

    /**
     * Init Rectangle for oval drawing
     * and init oval paint
     */
    private fun initOvalRect() {
        ovalPaint = Paint(paint)
        ovalPaint.style = Paint.Style.FILL
        ovalPaint.strokeWidth = 0f
        ovalRect = RectF(viewWidth / 2 - ovalRadius + strokeWidth, viewWidth / 2 - ovalRadius + strokeWidth, viewWidth / 2 + ovalRadius, viewWidth / 2 + ovalRadius)
    }

    /**
     * Init rectangles for lines drawing
     */
    private fun initLayersRects() {
        val size = ((strokeWidth + layerGap) * 2 * layerCount + ovalRadius * 2).toInt()

        viewHeght = size
        viewWidth = size
        for (i in 0 until layerCount) {
            val rectF = RectF(i * (strokeWidth + layerGap) + strokeWidth, i * (strokeWidth + layerGap) + strokeWidth, size - i * (strokeWidth + layerGap), size - i * (strokeWidth + layerGap))
            layersRects.add(rectF)
        }
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LayerProgressBar)
        layerCount = typedArray.getInt(R.styleable.LayerProgressBar_lpb_layerCount, DEFAULT_LAYER_COUNT)
        strokeWidth = typedArray.getDimension(R.styleable.LayerProgressBar_lpb_layerWidth, DEFAULT_LAYER_WIDTH)
        layerGap = typedArray.getDimension(R.styleable.LayerProgressBar_lpb_layerGap, DEFAULT_LAYER_GAP)
        layerLength = typedArray.getInt(R.styleable.LayerProgressBar_lpb_layerLength, DEFAULT_LAYER_LENGTH)
        ovalRadius = typedArray.getDimension(R.styleable.LayerProgressBar_lpb_ovalRadius, DEFAULT_OVAL_RADIUS)
        paint.color = typedArray.getColor(R.styleable.LayerProgressBar_lpb_color, resources.getColor(DEFAULT_COLOR))
        isMultiThreading = typedArray.getBoolean(R.styleable.LayerProgressBar_lpb_multiThreading, false)
        typedArray.recycle()
        initFigures()
        startProgress()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(viewWidth + Math.round(strokeWidth), viewHeght + Math.round(strokeWidth))
    }

    fun startProgress() {
        isRunning = true
        if (isMultiThreading) startMultiThread()
        else startSingleThread()
    }

    /**
     * Start animation in a single thread
     * (using valueAnimator)
     */
    private fun startSingleThread() {
        animator = ValueAnimator()
        animator.duration = animationDuration
        animator.setFloatValues(0f, 360f)
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener {
            currentPosition = it.animatedValue as Float
            post { invalidate() }
        }
        animator.start()
    }

    /**
     * Start animation in a multi-thread mode
     * each line has its own thread
     */
    private fun startMultiThread() {
        for (i in 0 until layerCount) {
            val pos = (250 * i) % 360
            currentPositions.add(pos.toFloat())
            val gap = (Math.random() * 4).toFloat() + 1
            Thread {
                while (isRunning) {
                    currentPositions[i] = (currentPositions[i] + gap) % 360
                    post {
                        invalidate()
                    }
                    Thread.sleep(16)
                }
            }.start()
        }
    }

    fun stopProgress() {
        if (isMultiThreading) isRunning = false
        else animator.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Drawing circle in center
        canvas.drawOval(ovalRect, ovalPaint)

        // Drawing lines
        if (isMultiThreading) {
            layersRects.forEachIndexed { index, rectF ->
                canvas.drawArc(rectF, if (index % 2 == 0) currentPositions[index] else 360 - currentPositions[index], layerLength.toFloat(), false, paint)
            }
        } else {
            layersRects.forEachIndexed { index, rectF ->
                canvas.drawArc(rectF, if (index % 2 == 0) currentPosition else 360 - currentPosition, layerLength.toFloat(), false, paint)
            }
        }
    }

    private fun dpToPx(dp: Float): Float {
        val metrics = Resources.getSystem().getDisplayMetrics()
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px).toFloat()
    }
}