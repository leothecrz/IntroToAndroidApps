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


public class HomeActivity extends AppCompatActivity {

    public final static String audioIntentDataName = "audioShouldBeRunning";
    private AudioHandler audioHandler;

    private boolean wasAudioRunningBeforePause;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        audioHandler = AudioHandler.getInstance();
        if(savedInstanceState != null) {
            boolean audioState = savedInstanceState.getBoolean(audioIntentDataName, true);
            if(audioState){
                audioHandler.play(getApplicationContext());
            }else{
                audioHandler.stop();
            }
        } else {
            audioHandler.play(getApplicationContext());
        }

        OnClickListener homeListener = view -> {
            switch (view.getId()){
                case (R.id.homeExitButton):{
                    this.finish();
                    break;
                }
                case (R.id.homePlayButton):{

                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    i.putExtra(audioIntentDataName, audioHandler.isRunningStatus());
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(audioHandler.isRunningStatus()){
            wasAudioRunningBeforePause = false;
        } else {
            wasAudioRunningBeforePause = true;
            audioHandler.stop();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wasAudioRunningBeforePause){
            audioHandler.play(getApplicationContext());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(HomeActivity.audioIntentDataName, audioHandler.isRunningStatus());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}