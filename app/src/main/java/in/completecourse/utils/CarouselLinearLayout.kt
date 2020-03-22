package `in`.completecourse.utils

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout

class CarouselLinearLayout : LinearLayout {
    /* renamed from: a */
    private var scale = 1.0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //The main mechanism to display scale animation, you can customize it as your needs
        val w = this.width.toFloat()
        val h = this.height.toFloat()
        canvas.scale(scale, scale, w / 2, h / 2)
    }

    fun setScaleBoth(scale: Float) {
        this.scale = scale
        invalidate()
    }
}