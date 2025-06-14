package br.ufv.dpi.inf311.pratica4;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends Activity  implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor proximitySensor;
    private LanternaHelper lanternaHelper;
    private MotorHelper motorHelper;
    private boolean on;
    private float updatedLightValue;
    private float updatedProximityValue;
    private final int REQUEST_CODE = 61;
    SwitchMaterial flashLightSwitch;
    SwitchMaterial vibrationSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnClassify = findViewById(R.id.btnClassify);
        btnClassify.setOnClickListener(classify());
        lanternaHelper = new LanternaHelper(this);
        motorHelper = new MotorHelper(this);
        flashLightSwitch = findViewById(R.id.switchFlashlight);
        vibrationSwitch = findViewById(R.id.switchVibration);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lightSensor != null && proximitySensor != null){
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_GAME);
            Log.i("SENSOR", proximitySensor.getName() + " e " + lightSensor.getName());
        }

    }

    private View.OnClickListener classify(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //TODO: perform classification
                Intent it = new Intent("ACTION_CLASSIFICATION");
                it.putExtra("lightValue",updatedLightValue);
                it.putExtra("proximityValue",updatedProximityValue);
                startActivityForResult(it,REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lanternaHelper.desligar();
        motorHelper.pararVibracao();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            updatedLightValue = sensorEvent.values[0];
            Log.i("SENSOR", "light value " + updatedLightValue);
        }else if(sensor.getType() == Sensor.TYPE_PROXIMITY){
            updatedProximityValue = sensorEvent.values[0];
            Log.i("SENSOR","proximity value "+ updatedProximityValue);
         }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            Log.i("SENSOR","Nada chegou, nada houve!");
        }else if(requestCode == REQUEST_CODE){
            if(resultCode == 3){
                boolean startLight = data.getBooleanExtra("startLight", false);
                boolean startVibration = data.getBooleanExtra("startVibration", false);
                if(startLight){
                    Log.i("SENSOR","Acende Luz");
                    lanternaHelper.ligar();
                    flashLightSwitch.setChecked(true);
                }else{
                    lanternaHelper.desligar();
                    flashLightSwitch.setChecked(false);
                }
                if(startVibration){
                    Log.i("SENSOR","Começa a vibrar");
                    motorHelper.iniciarVibracao();
                    vibrationSwitch.setChecked(true);
                }else{
                     motorHelper.pararVibracao();
                    vibrationSwitch.setChecked(false);
                }
            }
        }
    }
}