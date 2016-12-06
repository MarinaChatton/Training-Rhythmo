package com.chatton.marina.rhythmo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lukedeighton.wheelview.WheelView;

public class MainActivity extends AppCompatActivity {
    WheelView wheelView;
    TextView angleDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        angleDisplay = (TextView) findViewById(R.id.angle);

        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setOnWheelAngleChangeListener(new WheelView.OnWheelAngleChangeListener() {
            @Override
            public void onWheelAngleChange(float angle) {
                angleDisplay.setText(String.valueOf(angle));
            }
        });
    }
}
