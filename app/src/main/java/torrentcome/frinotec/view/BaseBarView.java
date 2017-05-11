package torrentcome.frinotec.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import torrentcome.frinotec.R;

public class BaseBarView extends View {

    private Paint mRectPaint;
    private Paint mLinePaint;
    private RectF mRectBounds = new RectF();
    private int mColorLeft;
    private int mColorDefault;
    private int top;
    private float mPhase = 1f;
    private int millisec;
    private ObjectAnimator mAnimatorY;

    public BaseBarView(Context context) {
        super(context);
        init(context);
    }

    public BaseBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectPaint.setColor(mColorDefault);
        mRectBounds.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(mRectBounds, mRectPaint);
        drawRectangleTop(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        int minh = getPaddingBottom() + getPaddingTop();
        int h = Math.max(MeasureSpec.getSize(heightMeasureSpec), minh);

        mRectBounds.set(0,
                0,
                w,
                h);
        setMeasuredDimension(w, h);
    }


    public void setPhase(float phase) {
        mPhase = phase;
    }

    public void setAnimStats(int millisec) {
        this.millisec = millisec;
    }

    public void animateStop() {
        if (mAnimatorY != null) {
            mAnimatorY.pause();
        }
    }

    public void animateStats() {
        mAnimatorY = ObjectAnimator.ofFloat(this, "phase", 0f, 1f);
        mAnimatorY.setDuration(millisec);
        mAnimatorY.setRepeatCount(ObjectAnimator.INFINITE);
        mAnimatorY.setRepeatMode(ObjectAnimator.RESTART);
        mAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mAnimatorY.start();
    }


    private void init(Context context) {
        final Resources resources = context.getResources();
        mColorLeft = resources.getColor(R.color.colorAccent);
        mColorDefault = resources.getColor(R.color.colorPrimaryLight);
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectBounds = new RectF();
        setWillNotDraw(false);
        top = 100;
    }

    private void drawRectangleTop(Canvas canvas) {
        int total = this.top;
        mLinePaint.setColor(mColorLeft);
        if (total > 0) {
            mRectBounds.set(0,
                    0,
                    getWidth(),
                    mPhase * (getHeight() * this.top / total));
            canvas.drawRect(mRectBounds, mLinePaint);
        }
    }
}