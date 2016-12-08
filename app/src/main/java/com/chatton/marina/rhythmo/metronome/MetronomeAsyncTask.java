package com.chatton.marina.rhythmo.metronome;

import android.os.AsyncTask;

/**
 * Created by Marina on 06/12/2016.
 */

public class MetronomeAsyncTask extends AsyncTask {
    Metronome metronome;

    public MetronomeAsyncTask() {
        metronome = new Metronome();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        metronome.play();
        return null;
    }

    public void stop() {
        metronome.stop();
        metronome = null;
    }

    public void setBPM(double bpm) {
        if (metronome != null) {
            metronome.setBpm(bpm);
            metronome.calcSilence();
        }
    }
}
