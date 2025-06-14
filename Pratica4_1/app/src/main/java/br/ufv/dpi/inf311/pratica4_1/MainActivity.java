package br.ufv.dpi.inf311.pratica4_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {
    private float lightValue;
    private float proximityValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btnReturnRate);
        btn.setOnClickListener(returnRating());
        Intent it = getIntent();
        if(it == null){
            btn.setEnabled(false);
        }else{
            lightValue = it.getFloatExtra("lightValue",Integer.MIN_VALUE);
            proximityValue = it.getFloatExtra("proximityValue",Integer.MIN_VALUE);
            Log.i("SENSOR","Recebeu valores lux: "+lightValue+" proximidade: "+proximityValue);
        }
    }
    private View.OnClickListener returnRating(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.putExtra("startLight",shouldTurnTheLightOn());
                it.putExtra("startVibration",shouldStartVibrating());
                setResult(3,it);
                finish();
            }
        };
    }
    private boolean shouldTurnTheLightOn(){
        return lightValue != Integer.MIN_VALUE && lightValue < 10.0;
    }
    private boolean shouldStartVibrating(){
        return proximityValue != Integer.MIN_VALUE && proximityValue < 3.0;
    }
}