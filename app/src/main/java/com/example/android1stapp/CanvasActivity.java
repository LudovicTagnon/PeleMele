package com.example.android1stapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class CanvasActivity extends AppCompatActivity {

    protected CanvasView canvas;
    private float vectorLength;
    private float vectorStroke;
    private float originX, originY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = this.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            originX = windowMetrics.getBounds().width() - insets.left - insets.right;
            originY = windowMetrics.getBounds().height() - insets.bottom - insets.top;
        }

        canvas = new CanvasView(CanvasActivity.this);
        setContentView(canvas);
    }

    protected class CanvasView extends View {
        private Paint paint;
        public CanvasView(Context context){
            super(context);
            setFocusable(true);


            //to check
            this.setWillNotDraw(false);
        }
        @Override
        public void onDraw(Canvas canvas){
            super.onDraw(canvas);
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setTextSize(30f);
            paint.setStrokeWidth(40);
            paint.setColor(Color.RED);

            //canvas.drawLine(originX, originY, 300, originY, paint);
            canvas.drawCircle(300, 300, 30, paint);
        }
    }

}