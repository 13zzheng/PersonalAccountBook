package stu.edu.cn.zing.personalbook.mywidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import stu.edu.cn.zing.personalbook.R;

/**
 * Created by Administrator on 2017/5/6.
 */

public class HorizaontalProgressBar extends View {

    private float max;

    private float progress;

    private float offset;

    private int color;


    private Paint mPaint;

    public HorizaontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HorizaontalProgressBar, defStyleAttr, 0);
        int count = array.getIndexCount();

        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.HorizaontalProgressBar_max:
                    max = array.getFloat(attr, 100f);
                    break;
                case R.styleable.HorizaontalProgressBar_progress:
                    progress = array.getFloat(attr, 0f);
                    break;
                case R.styleable.HorizaontalProgressBar_color:
                    color = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.HorizaontalProgressBar_offset:
                    offset = array.getFloat(attr, max/20);
                    break;
            }
        }
        array.recycle();
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    public HorizaontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizaontalProgressBar(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {

            if (progress < offset) {
                width = (int) (offset/max * widthSize);
            } else {
                width = (int) (progress/max * widthSize);

            }
        } else {
            width = 0;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = 20;
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(color);
    }


    public void setProgress(float progress) {
        this.progress = progress;


    }

    public void setColor(int color) {
        this.color = color;

    }

    public float getMax() {
        return max;
    }
}
