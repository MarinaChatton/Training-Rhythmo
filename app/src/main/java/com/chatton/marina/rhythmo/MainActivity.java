package com.chatton.marina.rhythmo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chatton.marina.rhythmo.metronome.Metronome;
import com.chatton.marina.rhythmo.metronome.MetronomeAsyncTask;
import com.lukedeighton.wheelview.WheelView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WheelView.OnWheelAngleChangeListener {
    private final static String IS_PLAYING_KEY = "isPlayingKey";

    //views
    WheelView wheelView;
    TextView rpmDisplay;
    TextView speedDisplay;
    FloatingActionButton onOffButton;

    //metronome
    MetronomeAsyncTask metronomeAsyncTask;
    boolean isPlaying = false;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isPlaying = savedInstanceState.getBoolean(IS_PLAYING_KEY);
        Log.e("PLAY", String.valueOf(isPlaying));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(IS_PLAYING_KEY, isPlaying);
        Log.e("PLAY", String.valueOf(isPlaying));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setOnWheelAngleChangeListener(this);

        rpmDisplay = (TextView) findViewById(R.id.display_rpm);
        speedDisplay = (TextView) findViewById(R.id.display_speed);

        onOffButton = (FloatingActionButton) findViewById(R.id.on_off_button);
        onOffButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            metronomeAsyncTask.stop();
            isPlaying = false;
        }else{
            metronomeAsyncTask = new MetronomeAsyncTask();
            metronomeAsyncTask.execute();
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
