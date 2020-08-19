package edu.stanford.cs108.mobiledraw;


import java.util.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.lang.Object.*;

import android.widget.EditText;
import android.widget.RadioButton;

public class CustomView extends View {
    protected static int mode;
    protected static float top, left, bottom, right, height, width;
    protected static float x, y, x1, x2, y1, y2;
    protected static int updateId = 0;
    protected static List<RectF> drawings  = new ArrayList<RectF>();
    protected static List<Integer> shapes = new ArrayList<Integer>();  // 0 for rect, 1 for oval
    protected static Paint outline1, outline2, filling1, filling2;



    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        // not selected
        outline1 = new Paint();
        outline1.setColor(Color.RED);
        outline1.setStyle(Paint.Style.STROKE);
        outline1.setStrokeWidth(5.0f);

        // currently selected
        outline2 = new Paint();
        outline2.setColor(Color.BLUE);
        outline2.setStyle(Paint.Style.STROKE);
        outline2.setStrokeWidth(15.0f);

        filling1 = new Paint();
        filling1.setColor(Color.WHITE);
        filling1.setStyle(Paint.Style.FILL);

        filling2 = new Paint();
        filling2.setColor(Color.WHITE);
        filling2.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int curId = getCurrId();
        GRect gRect = new GRect(left, top, right, bottom);
        GOval gOval = new GOval(left, top, right, bottom);
        RectF rectF = new RectF(left, top, right, bottom);
        RectF ovalF = new RectF(left, top, right, bottom);


        switch (mode) {

            // select mode
            case 0:
                displaySelection(curId);
                updateId = curId;
                for (int i = 0; i < drawings.size(); i++) {
                    if (shapes.get(i) == 0) {
                        gRect.paint(canvas, curId);
                    } else if (shapes.get(i) == 1) {
                        gOval.paint(canvas, curId);
                    }
                }
                break;

            // rect mode
            case 1:
                drawings.add(rectF);
                shapes.add(0);
                updateId = drawings.size() - 1;
                displaySelection(updateId);
                for (int i = 0; i < drawings.size(); i++) {
                    if (shapes.get(i) == 0) {
                        gRect.paint(canvas, updateId);
                    }else if (shapes.get(i) == 1) {
                        gOval.paint(canvas, updateId);
                    }
                }
                break;

            // oval mode
            case 2:
                drawings.add(ovalF);
                shapes.add(1);
                updateId = drawings.size() - 1;
                displaySelection(updateId);
                for (int i = 0; i < drawings.size(); i++) {
                    if (shapes.get(i) == 0) {
                        gRect.paint(canvas, updateId);
                    }else if (shapes.get(i) == 1) {
                        gOval.paint(canvas, updateId);
                    }
                }
                break;

            // erase mode
            case 3:
                drawings.remove(curId);
                shapes.remove(curId);
                displaySelection(-1);
                for (int i = 0; i < drawings.size(); i++) {
                    if (shapes.get(i) == 0) {
                        gRect.paint(canvas, -1);
                    } else if (shapes.get(i) == 1) {
                        gOval.paint(canvas, -1);
                    }
                }
                break;

        }

    }


    private int getCurrId() {
        int curId = -1;
        for (int i = drawings.size() - 1; i >= 0; i--) {
            if (drawings.get(i).top <= y &&
                    drawings.get(i).bottom >= y &&
                    drawings.get(i).left <= x &&
                    drawings.get(i).right >= x) {
                curId = i;
                break;
            }
        }
        return curId;
    }


    private void displaySelection(int id) {
        // not erase mode
        if (id >= 0 && mode <= 2) {
            RectF curRect = drawings.get(id);
            height = curRect.bottom - curRect.top;
            width = curRect.right - curRect.left;
            EditText et_x = (EditText)((Activity) getContext()).findViewById(R.id.x);
            et_x.setText(Float.toString(curRect.left));
            EditText et_y = (EditText)((Activity) getContext()).findViewById(R.id.y);
            et_y.setText(Float.toString(curRect.top));
            EditText et_h = (EditText)((Activity) getContext()).findViewById(R.id.height);
            et_h.setText(Float.toString(height));
            EditText et_w = (EditText)((Activity) getContext()).findViewById(R.id.width);
            et_w.setText(Float.toString(width));
        } else {
            EditText et_x = (EditText)((Activity) getContext()).findViewById(R.id.x);
            et_x.setText("");
            EditText et_y = (EditText)((Activity) getContext()).findViewById(R.id.y);
            et_y.setText("");
            EditText et_h = (EditText)((Activity) getContext()).findViewById(R.id.height);
            et_h.setText("");
            EditText et_w = (EditText)((Activity) getContext()).findViewById(R.id.width);
            et_w.setText("");
        }

    }


    // define the GObject class
    public class GObject extends Object {
        public GObject() {
        }
    }

    public class GRect extends GObject {
        private float left, top, right, bottom;

        public GRect(float left, float top, float right, float bottom) {
            super();
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public void paint(Canvas canvas, int index) {
            for (int i = 0; i < drawings.size(); i++) {
                if (shapes.get(i) == 0) {
                    if (i != index) {
                        canvas.drawRect(drawings.get(i), outline1);
                        canvas.drawRect(drawings.get(i), filling1);
                    } else {
                        canvas.drawRect(drawings.get(i), outline2);
                        canvas.drawRect(drawings.get(i), filling2);
                    }
                }
            }
        }
    }


    public class GOval extends GObject {
        private float left, top, right, bottom;

        public GOval(float left, float top, float right, float bottom) {
            super();
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public void paint(Canvas canvas, int index) {
            for (int i = 0; i < drawings.size(); i++) {
                if (shapes.get(i) == 1) {
                    if (i != index) {
                        canvas.drawOval(drawings.get(i), outline1);
                        canvas.drawOval(drawings.get(i), filling1);
                    } else {
                        canvas.drawOval(drawings.get(i), outline2);
                        canvas.drawOval(drawings.get(i), filling2);
                    }
                }

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        RadioButton selectBtn = (RadioButton)((Activity) getContext()).findViewById(R.id.select);
        RadioButton rectBtn = (RadioButton)((Activity) getContext()).findViewById(R.id.rect);
        RadioButton ovalBtn = (RadioButton)((Activity) getContext()).findViewById(R.id.oval);
        RadioButton eraseBtn = (RadioButton)((Activity) getContext()).findViewById(R.id.erase);

        if (selectBtn.isChecked()) {
            mode = 0;
        }

        if (rectBtn.isChecked()) {
            mode = 1;
        }

        if (ovalBtn.isChecked()) {
            mode = 2;
        }

        if (eraseBtn.isChecked()) {
            mode = 3;
        }

        switch (mode) {
            case 0:  // select
            case 3:  // erase
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        invalidate();
                        break;
                }
                break;


            case 1:  // rect
            case 2:  // oval
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();

                        if (x1 < x2) {
                            left = x1;
                            right = x2;
                        } else {
                            left = x2;
                            right = x1;
                        }

                        if (y1 < y2) {
                            top = y1;
                            bottom = y2;
                        } else {
                            top = y2;
                            bottom = y1;
                        }
                        invalidate();
                        break;
                }
                break;
        }
        return true;
    }

}
