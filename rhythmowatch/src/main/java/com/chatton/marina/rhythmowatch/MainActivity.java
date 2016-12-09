package com.chatton.marina.rhythmowatch;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chatton.marina.metronomelibrary.MetronomeAsyncTask;
import com.lukedeighton.wheelview.WheelView;

public class MainActivity extends Activity {
    private int defaultBpm = 80;

    WheelView wheelView;
    TextView rpmDisplay;
    ImageButton onOffButton;

    //metronome
    MetronomeAsyncTask metronomeAsyncTask;
    boolean isPlaying = false;
    int bpm = defaultBpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                initWheelview();

                initRpmDisplay();

                initOnOffButton();
            }
        });
    }

    public void initWheelview() {
        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setOnWheelAngleChangeListener(new WheelView.OnWheelAngleChangeListener() {
            @Override
            public void onWheelAngleChange(float angle) {
                bpm = calculateBpm(angle);
                rpmDisplay.setText(String.valueOf(bpm));
                if (metronomeAsyncTask != null) {
                    metronomeAsyncTask.setBPM(bpm);
                }
            }
        });
    }

    public void initRpmDisplay() {
        rpmDisplay = (TextView) findViewById(R.id.display_rpm);
        rpmDisplay.setText(String.valueOf(bpm));
    }

    public void initOnOffButton() {
        onOffButton = (ImageButton) findViewById(R.id.on_off_button);
        onOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    metronomeAsyncTask.stop();
                    isPlaying = false;
                } else {
                    metronomeAsyncTask = new MetronomeAsyncTask();
                    metronomeAsyncTask.execute();
                    metronomeAsyncTask.setBPM(bpm);
                    isPlaying = true;
                }
                setOnOffButtonColors(isPlaying);
            }
        });
        setOnOffButtonColors(isPlaying);
    }

    public int calculateBpm(float angle) {
        int bpm = defaultBpm + (int) (angle / 45) * 5;//add 5 bmp each 45°
        if (bpm >= 0) {
            return bpm;
        } else {
            return 0;
        }
    }

    private void setOnOffButtonColors(boolean isPlaying) {
        int iconColor;
        int backgroundColor;
        if (isPlaying) {
            iconColor = getResources().getColor(android.R.color.holo_blue_bright);
            backgroundColor = getResources().getColor(R.color.colorPrimary);
        } else {
            iconColor = getResources().getColor(R.color.colorDark);
            backgroundColor = getResources().getColor(R.color.colorPrimary);
        }
        onOffButton.getDrawable().setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        onOffButton.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
    }
}
