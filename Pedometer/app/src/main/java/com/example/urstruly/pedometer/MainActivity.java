package com.example.urstruly.pedometer;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static android.R.attr.value;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private TextView pedometer,detector;
    private Sensor stepcounter;
   // private Sensor stepdetector;
    private SensorManager sensorManager;
    private int laststepcount=-1;
   // private int detectorcount=0;
    private int substepcount;
    private int we=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        pedometer = (TextView)findViewById(R.id.pedometer);
        detector = (TextView)findViewById(R.id.Stepdetector);
        stepcounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        SharedPreferences sharedPreferences = getSharedPreferences("stepsCount", 0);
        String gettingstepcount=sharedPreferences.getString("count", null);
        if(gettingstepcount==null)
        {
            substepcount=0;
        }
        else
        {
            substepcount=Integer.parseInt(gettingstepcount);
        }

       // stepdetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,stepcounter,SensorManager.SENSOR_DELAY_FASTEST);
        //sensorManager.registerListener(this,stepdetector,SensorManager.SENSOR_DELAY_FASTEST);
        Log.v("myapp","resume");


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("myapp","stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //sensorManager.unregisterListener(this,stepdetector);
       /* if(laststepcount!=-1) {

        }*/
        sensorManager.unregisterListener(this,stepcounter);
        Log.v("myapp","destroy");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;
        int countervalue = -1;

        if (values.length > 0) {
            countervalue = (int) values[0];
        }
//

//
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(we==1) {
                if (countervalue - substepcount < 0) {
                    substepcount = 0;
                }
                we=0;
            }
            laststepcount=countervalue;
            SharedPreferences sharedPreferences = getSharedPreferences("stepsCount", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("count", Integer.toString(laststepcount));
            editor.apply();
            int ac=countervalue-substepcount;
            pedometer.setText("Step Counter Detected : " + countervalue);
            detector.setText("Step Detector Detected : " + substepcount);
        }
        /*if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            detectorcount++;
            detector.setText("Step Detector Detected : " + detectorcount);
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
