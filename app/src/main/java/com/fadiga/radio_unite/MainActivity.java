package com.fadiga.radio_unite;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {



    private static final String TAG = Constants.getLogTag("MainActivity");

    public String urlCh = "http://185.105.4.53:8020/stream";
    private MediaPlayer player;
    private ProgressBar spinner;
    private ImageView playButton, pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //setContentView(R.layout.activity_home);
        setupUI();
    }

    private void setupUI() {

        playButton = (ImageView) findViewById(R.id.playButton);
        spinner = (ProgressBar) findViewById(R.id.spinner);
        playButton.setOnClickListener(this);
        pauseButton = (ImageView) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this);
        displayMediaPlayerBtn(false);
        initializeMediaPlayer(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View v) {

        if (v == playButton) {
            initializeMediaPlayer(true);
        }
        if (v == pauseButton) {
            initializeMediaPlayer(false);
        }
    }

    private void initializeMediaPlayer(boolean play) {
        Log.d(TAG, "initializeMediaPlayer");
        stopPlaying();
        if (play){
            if (!Tools.isOnline(this)){
                Tools.toast(this, R.string.required_connexion_radio);
                return;
            }
            player = new MediaPlayer();
            displayMediaPlayerBtn(true);
            try {
                player.setDataSource(urlCh);
            } catch (IllegalArgumentException e) {
                Log.d(TAG, "IllegalArgumentException" + e.toString());
            } catch (IllegalStateException e) {
                Log.d(TAG, "IllegalStateException" + e.toString());
            } catch (IOException e) {
                Log.d(TAG, "IOException" + e.toString());
            }
            startPlaying();

        } else {
            displayMediaPlayerBtn(false);
        }
        //displayMediaPlayerBtn(false);
        Log.d(TAG, "fin initializeMediaPlayer");
    }


    public void displayMediaPlayerBtn(boolean play) {
        Log.d(TAG, "displayMedaPlayerBtn play: " + play);
        playOrPause(play, pauseButton, playButton);
        toggleSpinner(play, spinner);
    }

    public void playOrPause (boolean v, ImageView pauseButtonCh, ImageView playButtonCh){
        if (v) {
            goneOrVisible(pauseButtonCh, playButtonCh);
        } else {
            goneOrVisible(playButtonCh, pauseButtonCh);
        }
    }

    public void toggleSpinner(boolean play, ProgressBar spSelf) {
        if (play) {
            showSpinner(spSelf);
        } else {
            hideSpinner(spSelf);
        }
    }

    public void showSpinner(ProgressBar spinner) {
        spinner.setVisibility(View.VISIBLE);
    }
    public void hideSpinner(ProgressBar spinner) {
        spinner.setVisibility(View.GONE);
    }

    public void goneOrVisible(ImageView vbutton, ImageView gbutton) {
        vbutton.setVisibility(View.VISIBLE);
        gbutton.setVisibility(View.GONE);
    }
    private void startPlaying() {
        Log.d(TAG, "startPlaying");
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "Audio started");
                hideSpinner(spinner);
                player.start();
            }
        });
    }
    private void stopPlaying() {
        Log.d(TAG, "stopPlaying");
        try {
            //if (player.isPlaying()) {
            player.stop();
            player.release();
            // }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent a = new Intent(MainActivity.this, About.class);
            startActivity(a);
        }if (id == R.id.action_chare) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = Constants.share;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Partager ..."));
        }
        return super.onOptionsItemSelected(item);
    }
}
