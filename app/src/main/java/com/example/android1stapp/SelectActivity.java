package com.example.android1stapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class SelectActivity extends AppCompatActivity {

    protected ImageView imageView;
    protected Rect rect;
    protected SelectActivity.CanvasView canvasView;
    protected SurfaceView surfaceView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        imageView = findViewById(R.id.imageViewRandom);

        imageView.setOnTouchListener(
                new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getPointerCount() == 2) {

                            rect = new Rect((int) event.getX(0), (int) event.getY(0)-100, (int) event.getX(1), (int) event.getY(1)-100);
                            rect.sort();

                            canvasView = new CanvasView(SelectActivity.this);
                            SelectActivity.this.addContentView(canvasView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


                            canvasView.reDraw();



                        }

                        if(event.getAction() == android.view.MotionEvent.ACTION_UP){
                            Log.d("TouchTest", "Touch up");

                            View z = imageView.getRootView();
                            z.setDrawingCacheEnabled(true);
                            z.buildDrawingCache(true);

                            Bitmap bm0 = Bitmap.createBitmap(z.getDrawingCache());
                            Bitmap bm = Bitmap.createBitmap(bm0, (int) rect.left+50, (int) imageView.getY()+ 350 +rect.top, rect.width(), rect.height());






                            ImageView imageView2 = new ImageView(SelectActivity.this);
                            imageView2.setImageBitmap(bm);

                            Dialog dialog = new Dialog(SelectActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {

                                }
                            });

                            dialog.addContentView(imageView2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            dialog.show();

                        }

                        return true;
                    }
                }
        );

    }

    private class CanvasView extends View {

        private Paint paint;

        public CanvasView(Context context) {
            super(context);

            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(20);
            paint.setColor(Color.RED);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas = surfaceView.getHolder().lockCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            canvas.drawRect(rect, paint);
            surfaceView.getHolder().unlockCanvasAndPost(canvas);

        }


        public void reDraw() {
            this.invalidate();
        }

    }
}