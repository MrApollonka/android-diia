package ua.gov.diia.ui_base.views.common

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import ua.gov.diia.core.util.extensions.context.getColorCompat
import ua.gov.diia.ui_base.R
import kotlin.math.min

class DiiaStatusDot @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attr, defStyleAttr) {

    private val _paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var _enabled: Boolean
    private var _radius = 0f

    @ColorRes
    private var _colorEnabled: Int

    @ColorRes
    private var _colorDisabled: Int

    init {
        context.theme.obtainStyledAttributes(
            attr,
            R.styleable.DiiaStatusDot,
            defStyleAttr,
            0
        ).apply {
            try {
                _colorDisabled =
                    getResourceId(R.styleable.DiiaStatusDot_colorDisabled, R.color.black_alpha_30)
                _colorEnabled =
                    getResourceId(R.styleable.DiiaStatusDot_colorEnabled, R.color.green_snack)
                _enabled = getBoolean(R.styleable.DiiaStatusDot_isEnabled, true)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 8.dp
        val desiredHeight = 8.dp

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            else -> desiredWidth
        }

        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        _radius = (min(width, height) / 2.0).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        val colorRes = if (_enabled) _colorEnabled else _colorDisabled
        _paint.color = context.getColorCompat(colorRes)

        val cx = (width / 2).toFloat()
        val cy = (height / 2).toFloat()
        canvas.drawCircle(cx, cy, _radius, _paint)
    }

    fun setColorEnabled(@ColorRes res: Int) {
        if (res != 0) {
            _colorEnabled = res
            invalidate()
        }
    }

    fun setColorDisabled(@ColorRes res: Int) {
        if (res != 0) {
            _colorDisabled = res
            invalidate()
        }
    }

    fun setIsEnabled(enabled: Boolean) {
        _enabled = enabled
        invalidate()
    }

    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}