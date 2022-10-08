package com.example.android1stapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CapteursActivity extends AppCompatActivity {

    protected CapteursActivity.CanvasView canvas;
    private float vectorLength;
    private float vectorStroke;
    private float originX, originY;

    //----------------------------------------------------------------------------

    private LocalDateTime timer;

    //----------------------------------------------------------------------------

    TextView txt_currentAccel, txt_prevAccel, txt_Acceleration;

    private SensorManager SensorManager;
    private Sensor mAccelerometer;

    private double accelerationCurrentValue;
    private double accelerationPreviousValue;
    private double changeInAcceleration;

    private float xAccel, yAccel, zAccel;

    //-------------------------------------------------------------------------------
    //MAGNETOMETRE

    private ImageView imageViewAiguille;

    private Sensor sensorMagneticField;

    private float[] floatMagnetic = new float[3];
    private float[]  floatGravity = new float[3];
    private float[] floatOrientation = new float[3];
    private float[] floatRotationMatrix = new float[9];
    //-------------------------------------------------------------------------------

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capteurs);

        //----------------------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timer = LocalDateTime.now();
        }

        imageViewAiguille = findViewById(R.id.aiguille);

        //----------------------------------------------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = this.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            originX = windowMetrics.getBounds().width() - insets.left - insets.right;
            originY = windowMetrics.getBounds().height() - insets.bottom - insets.top;
        }

        canvas = new CanvasView(CapteursActivity.this);
        this.addContentView(canvas, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));


        //----------------------------------------------------------------------
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = SensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        txt_currentAccel = (TextView) findViewById(R.id.textViewCurrentAcceleration);
        txt_prevAccel = (TextView) findViewById(R.id.textViewPrevAccel);
        txt_Acceleration = (TextView) findViewById(R.id.textViewAcceleration);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchSensor = findViewById(R.id.switchAccelerometre);


        //----------------------------------------------------------------------
        if (switchSensor != null) {
            switchSensor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(CapteursActivity.this, "Active", Toast.LENGTH_SHORT).show();
                        switchSensor.setText("Active");
                        switchSensor.setChecked(true);
                        onResume();

                    } else {

                        Toast.makeText(CapteursActivity.this, "Desactive", Toast.LENGTH_SHORT).show();
                        switchSensor.setText("Desactive");
                        switchSensor.setChecked(false);
                        onPause();
                    }
                }
            });
        }

        switchSensor.setText("Active");
        switchSensor.setChecked(true);
        //Intent ic = new Intent(CapteursActivity.this, CanvasActivity.class);
        //startActivity(ic);
    }

    private SensorEventListener sensorEventListenerAccelerometer = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            xAccel = sensorEvent.values[0];
            yAccel = sensorEvent.values[1];
            zAccel = sensorEvent.values[2];

            accelerationCurrentValue = Math.sqrt(xAccel * xAccel + yAccel * yAccel + zAccel * zAccel);
            changeInAcceleration = Math.abs(accelerationCurrentValue - accelerationPreviousValue);
            accelerationPreviousValue = accelerationCurrentValue;


            txt_currentAccel.setText("Current = " + accelerationCurrentValue);
            txt_prevAccel.setText("Prev = " + accelerationPreviousValue);
            txt_Acceleration.setText("Acceleration = " + changeInAcceleration);

            //-----------------------------------------------------------------------------------

            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            canvas.reDraw();

            floatGravity = sensorEvent.values;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private SensorEventListener sensorEventListenerMagnetic = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            floatMagnetic = sensorEvent.values;
            android.hardware.SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatMagnetic);
            android.hardware.SensorManager.getOrientation(floatRotationMatrix, floatOrientation);
            imageViewAiguille.setRotation((float) (-Math.toDegrees(floatOrientation[0]) - 90));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    protected void onResume() {
        super.onResume();
        SensorManager.registerListener(sensorEventListenerAccelerometer, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        SensorManager.registerListener(sensorEventListenerMagnetic, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        SensorManager.unregisterListener(sensorEventListenerAccelerometer);
        SensorManager.unregisterListener(sensorEventListenerMagnetic);

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
            setFocusable(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.d("ACCELERATION_CHANGE", String.valueOf(changeInAcceleration));
            canvas.drawLine(540F, 960F, 540F + 50 * xAccel, (float) (960 - 50 * yAccel), paint);
        }

        public void reDraw() {
            this.invalidate();
        }

    }
}