package com.ajaybadgujar.roadracerlite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MainMenu extends Activity {

    Intent musicIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        final Intent optionsIntent = new Intent(MainMenu.this, Options.class);
        Global.display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        musicIntent = new Intent(getApplicationContext(), BGMusic.class);
        Global.musicThread = new Thread() {
            @Override
            public void run() {

                startService(musicIntent);
            }
        };


        Global.musicThread.start();

        Button btn_startrace = (Button) findViewById(R.id.startGameBtn);

        Global.context = getApplicationContext();

        btn_startrace.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /* Start Game */
                Intent intent = new Intent(MainMenu.this, GameActivity.class);
                MainMenu.this.startActivity(intent);
            }
        });
        Button optionsButton = (Button) findViewById(R.id.optionsBtn);
        optionsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                MainMenu.this.startActivity(optionsIntent);
            }
        });

        Button exitGameBtn = (Button) findViewById(R.id.exitGameBtn);
        exitGameBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(musicIntent);
                System.exit(0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        stopService(musicIntent);
        System.exit(0);
    }

}
