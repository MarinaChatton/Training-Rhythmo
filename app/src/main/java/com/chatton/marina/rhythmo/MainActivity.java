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
    private final static String DEFAULT_BPM_KEY = "defaultBmpKey";

    private int defaultBpm = 80;

    //views
    WheelView wheelView;
    TextView rpmDisplay;
    TextView speedDisplay;
    FloatingActionButton onOffButton;

    //metronome
    MetronomeAsyncTask metronomeAsyncTask;
    boolean isPlaying = false;
    int bpm;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isPlaying = savedInstanceState.getBoolean(IS_PLAYING_KEY, false);
        defaultBpm = savedInstanceState.getInt(DEFAULT_BPM_KEY, 80);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(IS_PLAYING_KEY, isPlaying);
        savedInstanceState.putInt(DEFAULT_BPM_KEY, bpm);
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

        rpmDisplay.setText(String.valueOf(defaultBpm));
        setOnOffButtonColors(isPlaying);
        bpm = defaultBpm;
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
            metronomeAsyncTask.setBPM(bpm);
            isPlaying = true;
        }
        setOnOffButtonColors(isPlaying);
    }

    //onWheelviewAngleChangeListener
    @Override
    public void onWheelAngleChange(float angle) {
        bpm = calculateBpm(angle);
        rpmDisplay.setText(String.valueOf(bpm));
        if(metronomeAsyncTask!=null) {
            metronomeAsyncTask.setBPM(bpm);
        }
    }


    public int calculateBpm(float angle){
        int bpm = defaultBpm + (int)(angle/45)*5;//add 5 bmp each 45°
        if(bpm>=0){
            return bpm;
        }else{
            return 0;
        }
    }
}
