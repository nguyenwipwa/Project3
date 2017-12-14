package com.project.com.project3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

public class Cau3Activity extends AppCompatActivity implements SensorEventListener {
    SensorManager sm;
    Sensor acceleromer, orientation;
    int count = 0;
    ConstraintLayout layoutcompass;
    TextView tx, toado;
    ImageView laban;
    static int idmenu = 1;
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau3);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleromer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        orientation = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        tx = (TextView) findViewById(R.id.count);
        laban = (ImageView) findViewById(R.id.laban_ima);
        toado = (TextView) findViewById(R.id.toado);
        layoutcompass = (ConstraintLayout) findViewById(R.id.layoutcompass);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            eventAcceleromer(sensorEvent);
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            eventLaban(sensorEvent);
        }

    }

    public void eventLaban(SensorEvent sensorEvent) {
        float toado = sensorEvent.values[0];
        int td = (int) toado;
        this.toado.setText(td + "");
        laban.setRotation(-toado);
    }

    public void eventAcceleromer(SensorEvent sensorEvent) {

        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float luclac = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        if (luclac > 2.0 && count <= 100) {
            count++;
        } else {
            if (count > 0)
                count--;
        }
        if (count == 100) {
            v.vibrate(1000);
        }
        tx.setText(count + "");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        if (idmenu == 1)
            sm.registerListener(this, acceleromer, SensorManager.SENSOR_DELAY_NORMAL);
        if (idmenu == 2)
            sm.registerListener(this, orientation, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        sm.unregisterListener(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Accelerometer:
                idmenu = 1;
                layoutcompass.setVisibility(View.GONE);
                tx.setVisibility(View.VISIBLE);

                onResume();
                break;
            case R.id.Orientation:
                idmenu = 2;
                layoutcompass.setVisibility(View.VISIBLE);
                tx.setVisibility(View.GONE);
                onResume();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
