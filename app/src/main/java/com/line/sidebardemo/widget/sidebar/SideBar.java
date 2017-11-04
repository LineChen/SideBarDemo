package com.line.sidebardemo.widget.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Beiing
 */
public class SideBar extends View {

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    /**
     * 显示的字符集--默认
     */
    private String[] characters = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "#"};

    /**
     * 选中标识
     */
    private int choose = 0;

    /**
     * 字母画笔
     */
    private Paint letterPaint;

    private Paint letterBgPaint;

    /**
     * 泡泡
     */
    private View bubbleView;

    /**
     * 字体大小
     */
    private int textSize = (int) (14 * getContext().getResources().getDisplayMetrics().density);

    /**
     * 默认字体颜色
     */
    private int defaultTextColor = Color.BLACK;

    /**
     * 选中字体颜色
     */
    private int selectedTextColor = Color.WHITE;

    /**
     * 触摸时背景颜色
     */
    private int touchedBgColor = Color.TRANSPARENT;

    private int letterBgColor = Color.RED;

    /**
     * 手指是否操作中,滑动过程中，不接受设置当前位置
     */
    private boolean isOnTouch;

    public SideBar(Context context) {
        this(context, null, 0);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        letterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        letterPaint.setTextSize(textSize);

        letterBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        letterBgPaint.setStyle(Paint.Style.FILL);
        letterBgPaint.setColor(letterBgColor);
    }


    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLetters(canvas);
    }

    private void drawLetters(Canvas canvas) {
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / characters.length;// 获取每一个字母的高度

        for (int i = 0; i < characters.length; i++) {
            if (!isInEditMode()) {
                letterPaint.setColor(defaultTextColor);
            }

            //坐标等于中间-字符串宽度的一半.
            Rect bounds = new Rect();
            letterPaint.getTextBounds("A", 0, 1, bounds);
            float textW = bounds.width();
            float xPos = width / 2 - textW / 2;
            float yPos = singleHeight * i + singleHeight;
            // 选中的状态
            if (i == choose) {
                letterPaint.setColor(selectedTextColor);
                letterPaint.setFakeBoldText(true);
                float cx = width / 2;
                float cy = bounds.exactCenterY() + (i + 1) * singleHeight;
                canvas.drawCircle(cx, cy, textW + 5f, letterBgPaint);
            }
            canvas.drawText(characters[i], xPos, yPos, letterPaint);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final int c = (int) (y / getHeight() * characters.length);// 点击y坐标所占总高度的比例*characters数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                isOnTouch = false;
                if (bubbleView != null) {
                    bubbleView.setVisibility(GONE);
                }
                setBackgroundColor(Color.TRANSPARENT);// 透明
                invalidate();
                break;

            default:
                isOnTouch = true;
                setBackgroundColor(touchedBgColor);// 选中时设置背景色
                if (oldChoose != c) {
                    if (c >= 0 && c < characters.length) {
                        choose = c;
                        invalidate();
                        if (onTouchingLetterChangedListener != null) {
                            onTouchingLetterChangedListener.onTouchingLetterChanged(characters[choose]);
                            if (bubbleView != null) {
                                bubbleView.setVisibility(VISIBLE);
                                int singleHeight = getHeight() / characters.length;// 获取每一个字母的高度
                                Rect bounds = new Rect();
                                letterPaint.getTextBounds("A", 0, 1, bounds);
                                float cy = bounds.exactCenterY() + (choose + 1) * singleHeight;
                                bubbleView.setTranslationY(cy - bubbleView.getHeight() / 2);
                            }
                        }
                    }
                }
                break;
        }
        return true;
    }


    /**
     * 设置当前显示的字母
     * @param letter
     */
    public void setCurrentLetter(String letter){
        if(!isOnTouch)
        for (int i = 0; i < characters.length; i++) {
            if(letter.compareToIgnoreCase(characters[i]) == 0){
                choose = i;
                invalidate();
                break;
            }
        }
    }

    /**
     * 设置显示字符集
     *
     * @param chs
     */
    public SideBar setCharacters(String[] chs) {
        if (chs != null && chs.length != 0)
            this.characters = chs;
        return this;

    }

    /**
     * 设置提示框
     */
    public void setBubbleView(View bubbleView) {
        this.bubbleView = bubbleView;
        this.bubbleView.setVisibility(GONE);
    }

    /**
     * 设置选中时显示的字体颜色
     *
     * @param color
     */
    public SideBar setSelectedTextColor(int color) {
        if (color != 0)
            this.selectedTextColor = color;
        return this;
    }

    /**
     * 设置默认显示的字体颜色
     *
     * @param color
     */
    public SideBar setDefaultTextColor(int color) {
        if (color != 0)
            this.defaultTextColor = color;
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public SideBar setTextSize(int textSize) {
        if (textSize != 0)
            this.textSize = textSize;
        return this;
    }

    /**
     * 设置触摸时背景色
     *
     * @param color
     */
    public SideBar setTouchedBgColor(int color) {
        if (color != 0)
            this.touchedBgColor = color;
        return this;
    }

    public SideBar setLetterBgColor(int color) {
        if (color != 0)
            this.letterBgColor = letterBgColor;
        return this;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}