package com.chatton.marina.rhythmo;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.location.Location;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chatton.marina.rhythmo.metronome.Metronome;
import com.chatton.marina.rhythmo.metronome.MetronomeAsyncTask;
import com.lukedeighton.wheelview.WheelView;
import com.mindandgo.locationdroid.LocationDroid;
import com.xavierbauquet.theo.Theo;
import com.xavierbauquet.theo.annotations.location.AccessCoarseLocation;
import com.xavierbauquet.theo.annotations.location.AccessFineLocation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WheelView.OnWheelAngleChangeListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private final static String DEFAULT_BPM_KEY = "defaultBmpKey";

    SharedPreferences sharedPreferences;

    private int defaultBpm = 80;

    //views
    WheelView wheelView;
    TextView rpmDisplay;
    TextView speedDisplay;
    FloatingActionButton onOffButton;

    //metronome
    MetronomeAsyncTask metronomeAsyncTask;
    boolean isPlaying = false;
    int bpm = defaultBpm;

    //location
    LocationDroid locationDroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //defaults
        sharedPreferences = getPreferences(MODE_PRIVATE);

        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setOnWheelAngleChangeListener(this);

        rpmDisplay = (TextView) findViewById(R.id.display_rpm);

        speedDisplay = (TextView) findViewById(R.id.display_speed);

        onOffButton = (FloatingActionButton) findViewById(R.id.on_off_button);
        onOffButton.setOnClickListener(this);
        setOnOffButtonColors(isPlaying);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //load preferences values
        defaultBpm = sharedPreferences.getInt(DEFAULT_BPM_KEY, 80);
        bpm = defaultBpm;

        rpmDisplay.setText(String.valueOf(defaultBpm));
    }

    @Override
    protected void onStop() {
        if(locationDroid!=null){
            locationDroid.stop();
        }

        //save preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DEFAULT_BPM_KEY, bpm);
        editor.commit();

        super.onStop();
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

    //onOffButtonClickListener
    @Override
    public void onClick(View v) {
        if (isPlaying) {
            metronomeAsyncTask.stop();
            isPlaying = false;

            if(locationDroid!=null){
                locationDroid.stop();
                speedDisplay.setText(getResources().getString(R.string.speed_display_default));
            }
        } else {
            metronomeAsyncTask = new MetronomeAsyncTask();
            metronomeAsyncTask.execute();
            metronomeAsyncTask.setBPM(bpm);
            isPlaying = true;

            location();
        }
        setOnOffButtonColors(isPlaying);
    }

    //onWheelviewAngleChangeListener
    @Override
    public void onWheelAngleChange(float angle) {
        bpm = calculateBpm(angle);
        rpmDisplay.setText(String.valueOf(bpm));
        if (metronomeAsyncTask != null) {
            metronomeAsyncTask.setBPM(bpm);
        }
    }

    public int calculateBpm(float angle) {
        int bpm = defaultBpm + (int) (angle / 45) * 5;//add 5 bmp each 45°
        if (bpm >= 0) {
            return bpm;
        } else {
            return 0;
        }
    }


    @AccessFineLocation
    private void location() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //check if GPS is activated
        locationDroid = new LocationDroid(this) {
            @Override
            public void onNewLocation(Location location) {
                speedDisplay.setText(location.getSpeed()+" "+getResources().getString(R.string.speed_unit));
            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

            @Override
            public void serviceProviderStatusListener(String s, int i, Bundle bundle) {

            }
        };

        try{
            locationDroid.start();
        }catch (SecurityException s) {
            Log.e("Permissions Error", s.toString());
        }
    }
}
