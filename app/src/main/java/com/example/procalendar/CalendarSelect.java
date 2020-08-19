package com.example.procalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.RectF;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CalendarSelect extends View {

    public CalendarSelect(Context context) {
        this(context, null);
    }

    public CalendarSelect(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarSelect(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //设置抗锯齿的参数
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    //日期的画笔
    private Paint dayPaint;
    private Paint currentDayPaint;
    private Paint selectDayPaint;

    //总体的宽高
    private float mWidth;
    private float mHeight;


    //日期的宽高
    private float dayWidth;
    private float dayHeight;

    private String[] weeks;

    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        dayWidth = mWidth / 7.0f;
        dayHeight = dayWidth;
        mHeight = dayHeight * 6;
        setMeasuredDimension((int) mWidth, (int) mHeight + 5);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        drawDay(canvas);
    }

    private Point pointTouch = new Point(0, 0);

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointTouch.x = (int) (event.getX() / dayWidth);
                pointTouch.y = (int) (event.getY() / dayHeight);
                if (point2Date(pointTouch) != 0) {
                    if (select > 0 && select2 > 0) {
                        select = point2Date(pointTouch);
                        select2 = -1;
                    } else if (point2Date(pointTouch) < select) {
                        select = point2Date(pointTouch);
                    } else {
                        select2 = point2Date(pointTouch);
                    }
                }
                invalidate();
                requestLayout();
                return false;
            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
        }

        return true;
    }

    //月份的显示格式
//    DecimalFormat df = new DecimalFormat("00");

    private void init() {

        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        dayPaint = new Paint();
        dayPaint.setTextSize(DensityUtil.sp2px(18));
        dayPaint.setColor(Color.DKGRAY);
        dayPaint.setTextAlign(Paint.Align.CENTER);

        currentDayPaint = new Paint();
        currentDayPaint.setStyle(Paint.Style.STROKE);
        currentDayPaint.setStrokeWidth(2);
        currentDayPaint.setColor(Color.rgb(0, 162, 161));

        selectDayPaint = new Paint();
        selectDayPaint.setStyle(Paint.Style.FILL);
        selectDayPaint.setColor(Color.rgb(0, 162, 161));
    }

    private int select = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private int select2 = -1;
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
    int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private void drawDay(Canvas canvas) {
        int howDay = getMonthOfDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        float cellWidth = dayWidth;
        float cellHeight = dayHeight;
        RectF rectFL;
        RectF rectFC;
        RectF rectFR;
        RectF rectF;
        Point point;
        String dayText;
        //当前月
        for (int i = 0; i < howDay; i++) {
            dayText = String.valueOf(i + 1);
            point = dayXY(i + 1);
            rectFL = new RectF(point.x * cellWidth, point.y * cellHeight + cellHeight / 4f, (point.x + 1) * cellWidth, (point.y + 1) * cellHeight - cellHeight / 4f);
            rectFC = new RectF(point.x * cellWidth, point.y * cellHeight + cellHeight / 4f, (point.x + 1) * cellWidth, (point.y + 1) * cellHeight - cellHeight / 4f);
            rectFR = new RectF(point.x * cellWidth, point.y * cellHeight + cellHeight / 4f, (point.x + 1) * cellWidth, (point.y + 1) * cellHeight - cellHeight / 4f);
            rectF = new RectF(point.x * cellWidth + cellWidth / 4f, point.y * cellHeight + cellHeight / 4f, (point.x + 1) * cellWidth - cellWidth / 4f, (point.y + 1) * cellHeight - cellHeight / 4f);

            canvas.drawText(dayText, point.x * cellWidth + cellWidth / 2f, getBaseLine(dayPaint, point.y * cellHeight + cellHeight / 2f), dayPaint);
            if (currentDay == i + 1 &&
                    currentMonth == calendar.get(Calendar.MONTH) &&
                    currentYear == calendar.get(Calendar.YEAR)) {
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.height() / 2, currentDayPaint);
            }
            if (i + 1 == select && select2 < 0) {
                dayPaint.setColor(Color.WHITE);
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.height() / 2f,
                        selectDayPaint);
            } else if (i + 1 == select) {
                dayPaint.setColor(Color.WHITE);
                canvas.drawRoundRect(rectFL, cellWidth / 2f, 0,
                        selectDayPaint);
            } else if (i + 1 > select && i + 1 < select2) {
                dayPaint.setColor(Color.WHITE);
                canvas.drawRect(rectFC, selectDayPaint);
            } else if (i + 1 == select2) {
                dayPaint.setColor(Color.WHITE);
                canvas.drawRect(rectFR,
                        selectDayPaint);
            } else {
                dayPaint.setColor(Color.DKGRAY);
            }
            canvas.drawText(dayText,
                    cellWidth * point.x + cellWidth / 2.0f,
                    getBaseLine(dayPaint, point.y * cellHeight + cellHeight / 2f),
                    dayPaint);
        }

    }

    public void monthChange(int i) {
        calendar.add(Calendar.MONTH, i);
    }


    private List<Point> pointList = new ArrayList<>();

    private Point dayXY(int i) {

        calendar.set(Calendar.DAY_OF_MONTH, i);

        //查看是星期几
        int x, y;
        x = calendar.get(Calendar.DAY_OF_WEEK);
        y = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);

        Point point = new Point();
        point.x = x - 1;

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        if (x < calendar.get(Calendar.DAY_OF_WEEK)) {
            point.y = y;
        } else {
            point.y = y - 1;
        }

        pointList.add(point);
        return point;
    }

    private int point2Date(Point point) {
        for (Point p : pointList) {
            if (point.equals(p.x, p.y)) {
                return pointList.indexOf(p) + 1;
            }
        }
        return 0;
    }

    public static int getMonthOfDay(int year, int month) {
        int day = 0;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            day = 29;
        } else {
            day = 28;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return day;
        }
        return 0;
    }

    //获取文字的基线
    private float getBaseLine(Paint paint, float centerY) {
        return centerY - (paint.getFontMetricsInt().bottom + paint.getFontMetricsInt().top) / 2f;
    }

//    class DayModel {
//
//        private float dWidth;
//        private float dHeight;
//
//        private String dText;
//
//        private int textColor;
//        private float textSize;
//
//        private float backgroundSize;
//
//        private int location_x;
//        private int location_y;
//
//        private boolean isCurrentDay;
//        private boolean isSelect;
//        private boolean isContain;
//
//        private DayModel(float width, float height) {
//            dWidth = width;
//            dHeight = height;
//        }
//
//        private void drawDay(Canvas canvas, Paint paint) {
//            backgroundSize = Math.min(mWidth, mHeight);
//            drawText(canvas, paint);
//        }
//
//        private void drawText(Canvas canvas, Paint paint) {
//            textSize = backgroundSize / 3;
//            paint.setTextSize(textSize);
//            paint.setColor(textColor);
//            paint.setStyle(Paint.Style.FILL);
//
//            Rect rect = new Rect();
//            paint.getTextBounds(dText, 0, dText.length(), rect);
//            int w = rect.width();
//            float x = location_x * mWidth + (mWidth - w) / 2;
//            float y = location_y * mHeight + (mHeight - textSize / 2) / 2;
//            canvas.drawText(dText,x,y,paint);
//        }
//    }

}
