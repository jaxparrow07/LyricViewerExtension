package com.lauzy.freedom.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Looper;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.text.BidiFormatter;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;


/**
 * Desc : 歌词
 * Author : Lauzy
 * Date : 2017/10/13
 * Blog : http://www.jianshu.com/u/e76853f863a9
 * Email : freedompaladin@gmail.com
 */
public class LrcView extends View {

    private static final String DEFAULT_CONTENT = "Empty";
    private List<Lrc> mLrcData;
    private TextPaint mTextPaint;
    private String mDefaultContent;
    private int mCurrentLine;
    private float mOffset;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mScaledTouchSlop;
    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private float mLrcTextSize;
    private float mLrcLineSpaceHeight;
    private int mTouchDelay;
    private int mNormalColor;
    private int mCurrentPlayLineColor;
    private float mNoLrcTextSize;
    private int mNoLrcTextColor;
    //是否拖拽中，否的话响应onClick事件
    private boolean isDragging;
    //用户开始操作
    private boolean isUserScroll;
    private boolean isAutoAdjustPosition = true;
    private Drawable mPlayDrawable;
    private boolean isShowTimeIndicator;
    private Rect mPlayRect;
    private Paint mIndicatorPaint;
    private float mIndicatorLineWidth;
    private float mIndicatorTextSize;
    private int mCurrentIndicateLineTextColor;
    private int mIndicatorLineColor;
    private float mIndicatorMargin;
    private float mIconLineGap;
    private float mIconWidth;
    private float mIconHeight;
    private boolean isEnableShowIndicator = true;
    private int mIndicatorTextColor;
    private int mIndicatorTouchDelay;
    private boolean isCurrentTextBold;
    private boolean isLrcIndicatorTextBold;
    private String mPlayDrawableStr;
    private Paint.Align mTextAlign;
    private Map<String, Object> mGetterMap;
    private Context mContext;

    public LrcView(Context context, BitmapDrawable bmp) {
        super(context);
        init(context, bmp);
    }

    public void updateGetterMap() {

        mGetterMap.put("mLrcTextSize", mLrcTextSize);
        mGetterMap.put("mLrcLineSpaceHeight", mLrcLineSpaceHeight);
        mGetterMap.put("mTouchDelay", mTouchDelay);
        mGetterMap.put("mIndicatorTouchDelay", mIndicatorTouchDelay);
        mGetterMap.put("mNormalColor", mNormalColor);
        mGetterMap.put("mCurrentPlayLineColor", mCurrentPlayLineColor);
        mGetterMap.put("mTextAlign", mTextAlign);
        mGetterMap.put("mNoLrcTextSize", mNoLrcTextSize);
        mGetterMap.put("mNoLrcTextColor", mNoLrcTextColor);
        mGetterMap.put("mIndicatorLineWidth", mIndicatorLineWidth);
        mGetterMap.put("mIndicatorTextSize", mIndicatorTextSize);
        mGetterMap.put("mIndicatorTextColor", mIndicatorTextColor);
        mGetterMap.put("mCurrentIndicateLineTextColor", mCurrentIndicateLineTextColor);
        mGetterMap.put("mIndicatorLineColor", mIndicatorLineColor);
        mGetterMap.put("mIndicatorMargin", mIndicatorMargin);
        mGetterMap.put("mIconLineGap", mIconLineGap);
        mGetterMap.put("mIconWidth", mIconWidth);
        mGetterMap.put("mIconHeight", mIconHeight);
        mGetterMap.put("isCurrentTextBold", isCurrentTextBold);
        mGetterMap.put("isLrcIndicatorTextBold", isLrcIndicatorTextBold);
    
    }

    private void init(Context context, BitmapDrawable playIcon) {

        this.mContext = context;
        mGetterMap = new HashMap<>();

        mLrcTextSize = sp2px(context, 15);
        mLrcLineSpaceHeight = dp2px(context, 20);

        mTouchDelay = 3500;
        mIndicatorTouchDelay = 2500;

        mNormalColor = Color.GRAY;
        mCurrentPlayLineColor = Color.BLUE;
        mTextAlign = Paint.Align.CENTER;

        mNoLrcTextSize = dp2px(context, 20);
        mNoLrcTextColor = Color.BLACK;

        mIndicatorLineWidth = dp2px(context, 0.5f);
        mIndicatorTextSize = sp2px(context, 13);
        mIndicatorTextColor = Color.GRAY;
        mCurrentIndicateLineTextColor = Color.GRAY;
        mIndicatorLineColor = Color.GRAY;
        mIndicatorMargin = dp2px(context, 5);
        mIconLineGap = dp2px(context, 3);

        mIconWidth = dp2px(context, 20);
        mIconHeight = dp2px(context, 20);

        isCurrentTextBold = false;
        isLrcIndicatorTextBold = false;

        mPlayDrawable = playIcon;
        
        updateGetterMap();
        setupConfigs(context);
    }

    private void setupConfigs(Context context) {
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mOverScroller = new OverScroller(context, new DecelerateInterpolator());
        mOverScroller.setFriction(0.1f);
//        ViewConfiguration.getScrollFriction();  默认摩擦力 0.015f

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(mTextAlign);
        mTextPaint.setTextSize(mLrcTextSize);
        mDefaultContent = DEFAULT_CONTENT;

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStrokeWidth(mIndicatorLineWidth);
        mIndicatorPaint.setColor(mIndicatorLineColor);
        mPlayRect = new Rect();
        mIndicatorPaint.setTextSize(mIndicatorTextSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mPlayRect.left = (int) mIndicatorMargin;
            mPlayRect.top = (int) (getHeight() / 2 - mIconHeight / 2);
            mPlayRect.right = (int) (mPlayRect.left + mIconWidth);
            mPlayRect.bottom = (int) (mPlayRect.top + mIconHeight);
            mPlayDrawable.setBounds(mPlayRect);
        }
    }

    public int getLrcWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public int getLrcHeight() {
        return getHeight();
    }

    public boolean isLrcEmpty() {
        return mLrcData == null || getLrcCount() == 0;
    }

    public int getLrcCount() {
        return mLrcData.size();
    }

    public void setLrcData(List<Lrc> lrcData) {
        resetView(DEFAULT_CONTENT);
        mLrcData = lrcData;
        invalidate();
    }
    
    public void setTypeFace(Typeface typeface){
        if(mTextPaint!=null){
            mTextPaint.setTypeface(typeface);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isLrcEmpty()) {
            drawEmptyText(canvas);
            return;
        }
        int indicatePosition = getIndicatePosition();
        mTextPaint.setTextSize(mLrcTextSize);
        mTextPaint.setTextAlign(mTextAlign);
        float y = getLrcHeight() / 2f;
        float x = getLrcWidth() / 2f + getPaddingLeft();
        for (int i = 0; i < getLrcCount(); i++) {
            if (i > 0) {
                y += (getTextHeight(i - 1) + getTextHeight(i)) / 2f + mLrcLineSpaceHeight;
            }
            if (mCurrentLine == i) {
                mTextPaint.setColor(mCurrentPlayLineColor);
                mTextPaint.setFakeBoldText(isCurrentTextBold);
            } else if (indicatePosition == i && isShowTimeIndicator) {
                mTextPaint.setFakeBoldText(isLrcIndicatorTextBold);
                mTextPaint.setColor(mCurrentIndicateLineTextColor);
            } else {
                mTextPaint.setFakeBoldText(false);
                mTextPaint.setColor(mNormalColor);
            }
            drawLrc(canvas, x, y, i);
        }

        if (isShowTimeIndicator) {
            mPlayDrawable.draw(canvas);
            long time = mLrcData.get(indicatePosition).getTime();
            float timeWidth = mIndicatorPaint.measureText(LrcHelper.formatTime(time));
            mIndicatorPaint.setColor(mIndicatorLineColor);
            canvas.drawLine(mPlayRect.right + mIconLineGap, getHeight() / 2f,
                    getWidth() - timeWidth * 1.3f, getHeight() / 2f, mIndicatorPaint);
            int baseX = (int) (getWidth() - timeWidth * 1.1f);
            float baseline = getHeight() / 2f - (mIndicatorPaint.descent() - mIndicatorPaint.ascent()) / 2 - mIndicatorPaint.ascent();
            mIndicatorPaint.setColor(mIndicatorTextColor);
            canvas.drawText(LrcHelper.formatTime(time), baseX, baseline, mIndicatorPaint);
        }
    }

    private HashMap<String, StaticLayout> mLrcMap = new HashMap<>();

    private void drawLrc(Canvas canvas, float x, float y, int i) {
        String text = mLrcData.get(i).getText();
        BidiFormatter.Builder builder = new BidiFormatter.Builder();
        builder.stereoReset(true);
        androidx.core.text.BidiFormatter formatter = builder.build();
        String formattedText = formatter.unicodeWrap(text);

        StaticLayout staticLayout = mLrcMap.get(formattedText);
        if (staticLayout == null) {
            mTextPaint.setTextSize(mLrcTextSize);
            staticLayout = new StaticLayout(formattedText, mTextPaint, getLrcWidth(),
                    Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
            mLrcMap.put(formattedText, staticLayout);
        }
        canvas.save();
        canvas.translate(x, y - staticLayout.getHeight() / 2f - mOffset);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    //中间空文字
    private void drawEmptyText(Canvas canvas) {
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mNoLrcTextColor);
        mTextPaint.setTextSize(mNoLrcTextSize);
        canvas.save();
        StaticLayout staticLayout = new StaticLayout(mDefaultContent, mTextPaint,
                getLrcWidth(), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
        canvas.translate(getLrcWidth() / 2f + getPaddingLeft(), getLrcHeight() / 2f);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    public void updateTime(long time) {
        if (isLrcEmpty()) {
            return;
        }
        int linePosition = getUpdateTimeLinePosition(time);
        if (mCurrentLine != linePosition) {
            mCurrentLine = linePosition;
            if (isUserScroll) {
                invalidateView();
                return;
            }
            ViewCompat.postOnAnimation(LrcView.this, mScrollRunnable);
        }
    }

    public String GetContentByLinePosition(int linePosition) {
        return mLrcData.get(linePosition).getText();
    }

    public long GetTimeByLinePosition(int linePosition) {
        return mLrcData.get(linePosition).getTime();
    }

    public int getUpdateTimeLinePosition(long time) {
        int linePos = 0;
        for (int i = 0; i < getLrcCount(); i++) {
            Lrc lrc = mLrcData.get(i);
            if (time >= lrc.getTime()) {
                if (i == getLrcCount() - 1) {
                    linePos = getLrcCount() - 1;
                } else if (time < mLrcData.get(i + 1).getTime()) {
                    linePos = i;
                    break;
                }
            }
        }
        return linePos;
    }

    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            isUserScroll = false;
            scrollToPosition(mCurrentLine);
        }
    };

    private Runnable mHideIndicatorRunnable = new Runnable() {
        @Override
        public void run() {
            isShowTimeIndicator = false;
            invalidateView();
        }
    };

    private void scrollToPosition(int linePosition) {
        float scrollY = getItemOffsetY(linePosition);
        final ValueAnimator animator = ValueAnimator.ofFloat(mOffset, scrollY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (float) animation.getAnimatedValue();
                invalidateView();
            }
        });
        animator.setDuration(300);
        animator.start();
    }

    private int getIndicatePosition() {
        int pos = 0;
        float min = Float.MAX_VALUE;
        //itemOffset 和 mOffset 最小即当前位置
        for (int i = 0; i < mLrcData.size(); i++) {
            float offsetY = getItemOffsetY(i);
            float abs = Math.abs(offsetY - mOffset);
            if (abs < min) {
                min = abs;
                pos = i;
            }
        }
        return pos;
    }

    private float getItemOffsetY(int linePosition) {
        float tempY = 0;
        for (int i = 1; i <= linePosition; i++) {
            tempY += (getTextHeight(i - 1) + getTextHeight(i)) / 2 + mLrcLineSpaceHeight;
        }
        return tempY;
    }

    private HashMap<String, StaticLayout> mStaticLayoutHashMap = new HashMap<>();

    private float getTextHeight(int linePosition) {
        String text = mLrcData.get(linePosition).getText();
        StaticLayout staticLayout = mStaticLayoutHashMap.get(text);
        if (staticLayout == null) {
            mTextPaint.setTextSize(mLrcTextSize);
            staticLayout = new StaticLayout(text, mTextPaint,
                    getLrcWidth(), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
            mStaticLayoutHashMap.put(text, staticLayout);
        }
        return staticLayout.getHeight();
    }

    private boolean overScrolled() {
        return mOffset > getItemOffsetY(getLrcCount() - 1) || mOffset < 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isLrcEmpty()) {
            return super.onTouchEvent(event);
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                removeCallbacks(mScrollRunnable);
                removeCallbacks(mHideIndicatorRunnable);
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
                isUserScroll = true;
                isDragging = false;
                break;

            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - mLastMotionY;
                if (Math.abs(moveY) > mScaledTouchSlop) {
                    isDragging = true;
                    isShowTimeIndicator = isEnableShowIndicator;
                }
                if (isDragging) {

//                    if (mOffset < 0) {
//                        mOffset = Math.max(mOffset, -getTextHeight(0) - mLrcLineSpaceHeight);
//                    }
                    float maxHeight = getItemOffsetY(getLrcCount() - 1);
//                    if (mOffset > maxHeight) {
//                        mOffset = Math.min(mOffset, maxHeight + getTextHeight(getLrcCount() - 1) + mLrcLineSpaceHeight);
//                    }
                    if (mOffset < 0 || mOffset > maxHeight) {
                        moveY /= 3.5f;
                    }
                    mOffset -= moveY;
                    mLastMotionY = event.getY();
                    invalidateView();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!isDragging && (!isShowTimeIndicator || !onClickPlayButton(event))) {
                    isShowTimeIndicator = false;
                    invalidateView();
                    performClick();
                }
                handleActionUp(event);
                break;
        }
//        return isDragging || super.onTouchEvent(event);
        return true;
    }

    private void handleActionUp(MotionEvent event) {
        if (isEnableShowIndicator) {
            ViewCompat.postOnAnimationDelayed(LrcView.this, mHideIndicatorRunnable, mIndicatorTouchDelay);
        }
        if (isShowTimeIndicator && mPlayRect != null && onClickPlayButton(event)) {
            isShowTimeIndicator = false;
            invalidateView();
            if (mOnPlayIndicatorLineListener != null) {
                mOnPlayIndicatorLineListener.onPlay(mLrcData.get(getIndicatePosition()).getTime(),
                        mLrcData.get(getIndicatePosition()).getText());
            }
        }
        if (overScrolled() && mOffset < 0) {
            scrollToPosition(0);
            if (isAutoAdjustPosition) {
                ViewCompat.postOnAnimationDelayed(LrcView.this, mScrollRunnable, mTouchDelay);
            }
            return;
        }

        if (overScrolled() && mOffset > getItemOffsetY(getLrcCount() - 1)) {
            scrollToPosition(getLrcCount() - 1);
            if (isAutoAdjustPosition) {
                ViewCompat.postOnAnimationDelayed(LrcView.this, mScrollRunnable, mTouchDelay);
            }
            return;
        }

        mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
        float yVelocity = mVelocityTracker.getYVelocity();
        float absYVelocity = Math.abs(yVelocity);
        if (absYVelocity > mMinimumFlingVelocity) {
            mOverScroller.fling(0, (int) mOffset, 0, (int) (-yVelocity), 0,
                    0, 0, (int) getItemOffsetY(getLrcCount() - 1),
                    0, (int) getTextHeight(0));
            invalidateView();
        }
        releaseVelocityTracker();
        if (isAutoAdjustPosition) {
            ViewCompat.postOnAnimationDelayed(LrcView.this, mScrollRunnable, mTouchDelay);
        }
    }

    private boolean onClickPlayButton(MotionEvent event) {
        float left = mPlayRect.left;
        float right = mPlayRect.right;
        float top = mPlayRect.top;
        float bottom = mPlayRect.bottom;
        float x = event.getX();
        float y = event.getY();
        return mLastMotionX > left && mLastMotionX < right && mLastMotionY > top
                && mLastMotionY < bottom && x > left && x < right && y > top && y < bottom;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset()) {
            mOffset = mOverScroller.getCurrY();
            invalidateView();
        }
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void resetView(String defaultContent) {
        if (mLrcData != null) {
            mLrcData.clear();
        }
        mLrcMap.clear();
        mStaticLayoutHashMap.clear();
        mCurrentLine = 0;
        mOffset = 0;
        isUserScroll = false;
        isDragging = false;
        mDefaultContent = defaultContent;
        removeCallbacks(mScrollRunnable);
        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 暂停（手动滑动歌词后，不再自动回滚至当前播放位置）
     */
    public void pause() {
        isAutoAdjustPosition = false;
        invalidateView();
    }

    /**
     * 恢复（继续自动回滚）
     */
    public void resume() {
        isAutoAdjustPosition = true;
        ViewCompat.postOnAnimationDelayed(LrcView.this, mScrollRunnable, mTouchDelay);
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }


    /*------------------Config-------------------*/

    private OnPlayIndicatorLineListener mOnPlayIndicatorLineListener;

    public void setOnPlayIndicatorLineListener(OnPlayIndicatorLineListener onPlayIndicatorLineListener) {
        mOnPlayIndicatorLineListener = onPlayIndicatorLineListener;
    }

    public interface OnPlayIndicatorLineListener {
        void onPlay(long time, String content);
    }

    public Object getVarFromMap(String var) {
        return mGetterMap.get(var);
    }


    public void setEmptyContent(String defaultContent) {
        mDefaultContent = defaultContent;
        invalidateView();
        updateGetterMap();
    }

    public void setLrcTextSize(float lrcTextSize) {
        mLrcTextSize = lrcTextSize;
        invalidateView();
        updateGetterMap();
    }

    public void setLrcLineSpaceHeight(float lrcLineSpaceHeight) {
        mLrcLineSpaceHeight = lrcLineSpaceHeight;
        invalidateView();
        updateGetterMap();
    }

    public void setTouchDelay(int touchDelay) {
        mTouchDelay = touchDelay;
        invalidateView();
        updateGetterMap();
    }

    public void setIndicatorTouchDelay(int touchDelay) {
        mIndicatorTouchDelay = touchDelay;
        invalidateView();
        updateGetterMap();
    }

    public void setNormalColor(@ColorInt int normalColor) {
        mNormalColor = normalColor;
        invalidateView();
        updateGetterMap();
    }

    public void setCurrentPlayLineColor(@ColorInt int currentPlayLineColor) {
        mCurrentPlayLineColor = currentPlayLineColor;
        invalidateView();
        updateGetterMap();
    }

    public void setTextAlignment(Object align) {
        mTextAlign = (Paint.Align) align;
        invalidateView();
        updateGetterMap();
    }

    public void setNoLrcTextSize(float noLrcTextSize) {
        mNoLrcTextSize = noLrcTextSize;
        invalidateView();
        updateGetterMap();
    }

    public void setNoLrcTextColor(@ColorInt int noLrcTextColor) {
        mNoLrcTextColor = noLrcTextColor;
        invalidateView();
        updateGetterMap();
    }

    public void setIndicatorLineWidth(float indicatorLineWidth) {
        mIndicatorLineWidth = indicatorLineWidth;
        invalidateView();
        updateGetterMap();
    }

    public void setIndicatorTextSize(float indicatorTextSize) {
//        mIndicatorTextSize = indicatorTextSize;
        mIndicatorPaint.setTextSize(indicatorTextSize);
        invalidateView();
    }

    public void setIndicatorTextColor(int indicatorTextColor) {
        mIndicatorTextColor = indicatorTextColor;
        invalidateView();
        updateGetterMap();
    }

    public void setCurrentIndicateLineTextColor(int currentIndicateLineTextColor) {
        mCurrentIndicateLineTextColor = currentIndicateLineTextColor;
        invalidateView();
        updateGetterMap();
    }

    public void setIndicatorLineColor(int indicatorLineColor) {
        mIndicatorLineColor = indicatorLineColor;
        invalidateView();
        updateGetterMap();
    }

    public void setIndicatorMargin(float indicatorMargin) {
        mIndicatorMargin = indicatorMargin;
        invalidateView();
        updateGetterMap();
    }

    public void setIconLineGap(float iconLineGap) {
        mIconLineGap = iconLineGap;
        invalidateView();
        updateGetterMap();
    }

    public void setIconWidth(float iconWidth) {
        mIconWidth = iconWidth;
        invalidateView();
        updateGetterMap();
    }

    public void setIconHeight(float iconHeight) {
        mIconHeight = iconHeight;
        invalidateView();
        updateGetterMap();
    }

    public void setPlayDrawable(BitmapDrawable bmp) {
        mPlayDrawable = bmp;
        mPlayDrawable.setBounds(mPlayRect);
        invalidateView();
    }

    public void setLrcCurrentTextBold(boolean bold) {
        isCurrentTextBold = bold;
        invalidateView();
        updateGetterMap();
    }

    public void setLrcIndicatorTextBold(boolean bold) {
        isLrcIndicatorTextBold = bold;
        invalidateView();
        updateGetterMap();
    }

    public void setEnableShowIndicator(boolean enableShowIndicator) {
        isEnableShowIndicator = enableShowIndicator;
        invalidateView();
        updateGetterMap();
    }

}
