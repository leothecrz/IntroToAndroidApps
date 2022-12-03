package cpp.concentrationgameapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View.OnClickListener;

/**
 * Handles HomePanel Activities
 * 2nd Activity
 *
 */
public class HomeActivity extends AppCompatActivity {

    private AudioHandler audioHandler;
    private boolean wasAudioRunningBeforePause;
    private Boolean audioSavedInstanceShouldBeRunning;
    private int mediaStopTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Action Bar Removal
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //Audio Initialization
        audioHandler = AudioHandler.getInstance();
        if(savedInstanceState != null) { // Saved Instance Extra Setup
            audioSavedInstanceShouldBeRunning = savedInstanceState.getBoolean("audioShouldBeRunning", false);
            mediaStopTime = savedInstanceState.getInt("mediaStopTime", 0);
        }
        wasAudioRunningBeforePause = true;

        //Control Over Home Window Buttons
        OnClickListener homeListener = view -> {
            switch (view.getId()){
                case (R.id.homeExitButton):{
                    this.finish();
                    break;
                }
                case (R.id.homePlayButton):{

                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    //i.putExtra(audioIntentDataName, audioHandler.isRunningStatus());
                    startActivity(i);
                    break;
                }
                case (R.id.homeHighScoresButton):{

                    Intent i = new Intent(getApplicationContext(), HighScoreActivity.class);
                    startActivity(i);
                }
                case (R.id.homeSettingsButton):{

                    if(!audioHandler.isRunningStatus()){
                        audioHandler.play(getApplicationContext());
                    } else {
                        audioHandler.stop();
                    }

                    break;
                }
                default: {
                    System.out.println(view);
                    Log.e("homeListener Failure: ", view.toString());
                    break;
                }
            }
        }; // OnClickListener

        Button homePlayButton = findViewById(R.id.homePlayButton);
        Button homeSettingsButton = findViewById(R.id.homeSettingsButton);
        Button homeExitButton = findViewById(R.id.homeExitButton);
        Button homeHighScoresButton = findViewById(R.id.homeHighScoresButton);

        homeExitButton.setOnClickListener(homeListener);
        homePlayButton.setOnClickListener(homeListener);
        homeSettingsButton.setOnClickListener(homeListener);
        homeHighScoresButton.setOnClickListener(homeListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //AudioResume
        if(wasAudioRunningBeforePause){
            audioHandler.play(getApplicationContext());
        }
        if(audioSavedInstanceShouldBeRunning != null){
            if(audioSavedInstanceShouldBeRunning){
                audioHandler.play(getApplicationContext(), mediaStopTime);
            } else {
                audioHandler.stop();
            }
            audioSavedInstanceShouldBeRunning = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //AudioPause
        if(!audioHandler.isRunningStatus()){
            wasAudioRunningBeforePause = false;
        } else {
            wasAudioRunningBeforePause = true;
            audioHandler.stop();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(audioHandler.isRunningStatus() && wasAudioRunningBeforePause){ // Pre-Pause
            outState.putBoolean("audioShouldBeRunning", true);
        } else { // Post-Pause
            outState.putBoolean("audioShouldBeRunning", wasAudioRunningBeforePause);
        }
        outState.putInt("mediaStopTime", AudioHandler.mediaStopTime);
    }

}