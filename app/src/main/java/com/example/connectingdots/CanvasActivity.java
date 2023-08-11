package com.example.connectingdots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CanvasActivity extends View {
    Paint paint_red = new Paint();
    Paint paint_blue = new Paint();
    Paint paint_green = new Paint();
    Paint paint_black = new Paint();
    private List<Dot> dots = new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();
    Dot selectedDot;
    private boolean checkIntersection() {
        for (int i = 0; i < connections.size(); i++)
            for (int j = i + 1; j < connections.size(); j++)
                if (connections.get(i).intersects(connections.get(j)))
                    return true;
        return false;
    }


    public CanvasActivity(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setValues();
    }

    public void setValues() {
        paint_red = new Paint();
        paint_blue = new Paint();
        paint_blue.setColor(Color.BLUE);
        paint_blue.setStrokeWidth(15);
        paint_red.setStrokeWidth(15);
        paint_red.setColor(Color.RED);
        paint_green = new Paint();
        paint_green.setColor(Color.GREEN);
        paint_green.setStrokeWidth(15);
        paint_black = new Paint();
        paint_black.setColor(Color.BLACK);
        paint_black.setStrokeWidth(15);
        dots.add(new Dot(300, 850, paint_red));
        dots.add(new Dot(825, 1025, paint_red));
        dots.add(new Dot(650, 500, paint_blue));
        dots.add(new Dot(425, 1025, paint_black));
        dots.add(new Dot(1000, 850, paint_green));
        dots.add(new Dot(425, 675, paint_green));
        dots.add(new Dot(650, 1200, paint_blue));
        dots.add(new Dot(825, 675, paint_black));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Dot dot : dots) {
            canvas.drawCircle(dot.getX(), dot.getY(), 80, dot.getPaint());
        }
        for (Connection connection : connections) {
            canvas.drawLine(connection.getStart().x, connection.getStart().y,
                    connection.getEnd().x, connection.getEnd().y, connection.getPaint());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (Dot dot : dots) {
                    if (dot.contains(touchX, touchY)) {
                        selectedDot = dot;
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (selectedDot != null) {
                    for (Dot dot : dots) {
                        if (dot != selectedDot && dot.contains(touchX, touchY) && dot.getPaint() == selectedDot.getPaint()) {
                            connections.add(new Connection(selectedDot.getCenter(), dot.getCenter(),selectedDot.getPaint()));
                            if(checkIntersection())
                            {
                                Toast.makeText(getContext(),"Intersected! Game Over!",Toast.LENGTH_SHORT).show();
                                dots.clear();
                                connections.clear();
                                setValues();
                            }
                            invalidate();
                            break;
                        }
                    }
                    selectedDot = null;
                }
                break;
                            default:
                                return false;
                        }

                        invalidate();
                        return true;
                    }

                    private class Dot {
                        private float x,y;
                        private Paint paint;

                        public Paint getPaint() {
                            return paint;
                        }

                        public void setPaint(Paint paint) {
                            this.paint = paint;
                        }

                        public Dot(float x, float y, Paint paint) {
                            this.x = x;
                            this.y = y;
                            this.paint=paint;
                        }

                        public boolean contains(float touchX, float touchY) {
                            float dx = touchX - x;
                            float dy = touchY - y;
                            float sum_of_squares = dx * dx + dy * dy;
                            return sum_of_squares <= 50 * 50;
                        }

                        public PointF getCenter() {
                            return new PointF(x, y);
                        }

                        public float getX() {
                            return x;
                        }

                        public void setX(float x) {
                            this.x = x;
                        }

                        public float getY() {
                            return y;
                        }

                        public void setY(float y) {
                            this.y = y;
                        }
                    }

                    private class Connection {
                        private PointF start;
                        private PointF end;
                        private RectF rectF;
                        private Paint paint;

                        public Paint getPaint() {
                            return paint;
                        }

                        public void setPaint(Paint paint) {
                            this.paint = paint;
                        }

                        public Connection(PointF start, PointF end, Paint paint) {
                            this.start = start;
                            this.end = end;
                            this.paint=paint;
                            float left = Math.min(start.x, end.x);
                            float top = Math.min(start.y, end.y);
                            float right = Math.max(start.x, end.x);
                            float bottom = Math.max(start.y, end.y);
                            rectF = new RectF(left, top, right, bottom);
                        }
                        public boolean intersects(Connection other) {
                            return RectF.intersects(rectF, other.rectF);
                        }

                        public PointF getStart() {
                            return start;
                        }

                        public PointF getEnd() {
                            return end;
                        }
                    }
                }
