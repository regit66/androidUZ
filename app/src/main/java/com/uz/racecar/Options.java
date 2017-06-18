package com.uz.racecar;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by Marek on 12.06.2017.
 */

public class Options extends Activity {

    public static int NUMBER_OF_ENEMIES = 5;
    public static float OFFSET_BETWEEN_ENEMIES = 5.5f;
    AudioManager am;
    SeekBar volumeBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxV = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curV = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        final ToggleButton muteBtn = (ToggleButton) findViewById(R.id.muteBtn);
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setProgress(curV);
        volumeBar.setMax(maxV);


        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                if (muteBtn.isChecked()) {
                    muteBtn.setChecked(false);
                    am.setStreamMute(AudioManager.STREAM_MUSIC, false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (muteBtn.isChecked()) {
                    am.setStreamMute(AudioManager.STREAM_MUSIC, true);
                } else {
                    am.setStreamMute(AudioManager.STREAM_MUSIC, false);
                }

            }
        });

        ArrayList<String> levelList = new ArrayList();

        levelList.add("Hard");
        levelList.add("Medium");
        levelList.add("Easy");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, levelList);
        final Spinner levelSpinner = (Spinner) findViewById(R.id.spinner);
        levelSpinner.setAdapter(arrayAdapter);
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(levelSpinner.getSelectedItem());
                if (levelSpinner.getSelectedItem() == "Hard") {
                    NUMBER_OF_ENEMIES = 6;
                    OFFSET_BETWEEN_ENEMIES = 2.5f;
                }
                if (levelSpinner.getSelectedItem() == "Medium") {
                    NUMBER_OF_ENEMIES = 5;
                    OFFSET_BETWEEN_ENEMIES = 3.5f;
                }
                if (levelSpinner.getSelectedItem() == "Easy") {
                    NUMBER_OF_ENEMIES = 3;
                    OFFSET_BETWEEN_ENEMIES = 3.5f;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}