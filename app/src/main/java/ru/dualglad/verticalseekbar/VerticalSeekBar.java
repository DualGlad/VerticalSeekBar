package ru.dualglad.verticalseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar {
    private static final float TO_VERTICAL = -90;
    private int last_progress;
    private OnSeekBarChangeListener onSeekBarChangeListener;


    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected synchronized void onDraw(Canvas canvas) {
        canvas.rotate(TO_VERTICAL);
        canvas.translate(-getHeight(), 0);
        super.onDraw(canvas);
    }

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onStartTrackingTouch(this);
                }
            case MotionEvent.ACTION_MOVE:
                setProgress(toProgress(event.getY()));
                setPressed(true);
                setSelected(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onStopTrackingTouch(this);
                }
                setPressed(false);
                setSelected(false);
                break;
            default:
                break;
        }
        return true;
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

    public synchronized void setProgress(int progress) {
        progress = Math.max(0, Math.min(getMax(), progress));
        if (progress != last_progress) {
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onProgressChanged(this, progress, true);
            }
            last_progress = progress;
            super.setProgress(progress);
            onSizeChanged(getWidth(), getHeight(), 0, 0);
        }
    }


    private int toProgress(float y) {
        int max = getMax();
        int height = getHeight();
        float interval = (float)height / (float)max;
        float half_interval = interval / 2;

        if (y < half_interval) {
            return max;
        }
        else if (y > (float)height - half_interval) {
            return 0;
        }
        else {
            return max - (int)((y + half_interval) / interval);
        }
    }
}
