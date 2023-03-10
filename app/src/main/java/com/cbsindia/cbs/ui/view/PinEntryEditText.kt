package com.cbsindia.cbs.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ActionMode
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.cbsindia.cbs.R

class PinEntryEditText : AppCompatEditText {
    private var space = 24f
    private var charSize = 0f
    private var numChars = 6f
    private var lineSpacing = 8f
    private var maxLength = 6
    private var clickListener: OnClickListener? = null
    private var lineStroke = 1f
    private var lineStrokeSelected = 2f
    private var linesPaint: Paint? = null
    private var placeHolder = "******"
    private val states = arrayOf(
        intArrayOf(android.R.attr.state_selected),
        intArrayOf(android.R.attr.state_focused),
        intArrayOf(-android.R.attr.state_focused)
    )
    private val colors = intArrayOf(
        Color.RED,
        Color.BLACK,
        Color.DKGRAY
    )
    private val colorStates = ColorStateList(states, colors)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val multi: Float = context.resources.displayMetrics.density
        lineStroke *= multi
        lineStrokeSelected *= multi
        linesPaint = Paint(paint)
        linesPaint?.strokeWidth = lineStroke

        setBackgroundResource(0)
        space *= multi
        lineSpacing *= multi
        maxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", 6)
        numChars = maxLength.toFloat()

        super.setOnClickListener { v ->
            setSelection(text!!.length)
            if (clickListener != null) {
                clickListener!!.onClick(v)
            }
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        clickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback?) {
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")
    }

    override fun onDraw(canvas: Canvas) {
        val availableWidth = width - paddingRight - paddingLeft
        charSize = if (space < 0) {
            availableWidth / (numChars * 2 - 1)
        } else {
            (availableWidth - space * (numChars - 1)) / numChars
        }
        var startX = paddingLeft
        val bottom = height - paddingBottom
        val textLength = text!!.length
        val textWidths = FloatArray(textLength)
        paint.getTextWidths(text, 0, textLength, textWidths)
        paint.color = ContextCompat.getColor(context, R.color.blue)
        var i = 0
        while (i < numChars) {
            updateColorForLines(i == textLength)
            canvas.drawLine(
                startX.toFloat(),
                bottom.toFloat(), startX + charSize, bottom.toFloat(), linesPaint!!
            )
            if (text!!.length > i) {
                val middle = startX + charSize / 2
                canvas.drawText(
                    placeHolder, i, i + 1, middle - textWidths[0] / 2, bottom - lineSpacing,
                    paint
                )
            }
            startX += if (space < 0) {
                (charSize * 2).toInt()
            } else {
                (charSize + space).toInt()
            }
            i++
        }
    }

    private fun getColorForState(vararg states: Int): Int {
        return colorStates.getColorForState(states, Color.GRAY)
    }

    private fun updateColorForLines(next: Boolean) {
        if (isFocused) {
            linesPaint?.strokeWidth = lineStrokeSelected
            linesPaint?.color = getColorForState(android.R.attr.state_focused)
            if (next) {
                linesPaint?.color = getColorForState(android.R.attr.state_selected)
            }
        } else {
            linesPaint?.strokeWidth = lineStroke
            linesPaint?.color = getColorForState(-android.R.attr.state_focused)
        }
    }

    companion object {
        const val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"
    }
}
