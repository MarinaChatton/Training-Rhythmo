package com.chatton.marina.rhythmo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chatton.marina.rhythmo.metronome.Metronome;
import com.lukedeighton.wheelview.WheelView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WheelView.OnWheelAngleChangeListener {
    //views
    WheelView wheelView;
    TextView rpmDisplay;
    TextView speedDisplay;
    FloatingActionButton onOffButton;

    //metronome
    Metronome metronome;
    boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        metronome = new Metronome();

        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setOnWheelAngleChangeListener(this);

        rpmDisplay = (TextView) findViewById(R.id.display_rpm);
        speedDisplay = (TextView) findViewById(R.id.display_speed);

        onOffButton = (FloatingActionButton) findViewById(R.id.on_off_button);
        onOffButton.setOnClickListener(this);
        setOnOffButtonColors(isPlaying);
    }

    private void setOnOffButtonColors(boolean isPlaying){
        int iconColor;
        int backgroundColor;
        if(isPlaying){
            iconColor = getResources().getColor(android.R.color.holo_blue_bright);
            backgroundColor = getResources().getColor(R.color.colorPrimary);
        }else{
            iconColor = getResources().getColor(R.color.colorDark);
            backgroundColor = getResources().getColor(R.color.colorPrimary);
        }
        onOffButton.getDrawable().setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        onOffButton.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
    }

    //onOffButtonClickListener
    @Override
    public void onClick(View v) {
        if(isPlaying){
            //metronome.stop();
            isPlaying = false;
        }else{
            //metronome.play();
            isPlaying = true;
        }
        setOnOffButtonColors(isPlaying);
    }

    //onWheelviewAngleChangeListener
    @Override
    public void onWheelAngleChange(float angle) {
        rpmDisplay.setText(String.valueOf(angle));
    }
}
