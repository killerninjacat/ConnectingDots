package com.example.connectingdots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CanvasActivity extends View {
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    Paint paint3 = new Paint();
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
        paint1 = new Paint();
        paint2 = new Paint();
        paint2.setColor(Color.BLUE);
        paint2.setStrokeWidth(8);
        paint1.setStrokeWidth(8);
        paint1.setColor(Color.RED);
        paint3 = new Paint();
        paint3.setColor(Color.GREEN);
        paint3.setStrokeWidth(8);
        dots.add(new Dot(800, 300, paint1));
        dots.add(new Dot(400, 500, paint1));
        dots.add(new Dot(400, 700, paint2));
        dots.add(new Dot(800, 500, paint2));
        dots.add(new Dot(400, 300, paint3));
        dots.add(new Dot(800, 700, paint3));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Dot dot : dots) {
            canvas.drawCircle(dot.getX(), dot.getY(), 50, dot.getPaint());
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
