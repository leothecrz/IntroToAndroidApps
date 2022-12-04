package cpp.concentrationgameapp;

import android.app.AlertDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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

                    showDialog();
                    break;

                }
                case (R.id.homeHighScoresButton):{

                    Intent i = new Intent(getApplicationContext(), HighScoreActivity.class);
                    startActivity(i);
                    break;
                }
                case (R.id.homeSettingsButton):{

                    if(!audioHandler.isRunningStatus()){
                        audioHandler.setEnabled(true);
                        audioHandler.play(getApplicationContext());
                    } else {
                        audioHandler.setEnabled(false);
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(audioHandler.isRunningStatus() && wasAudioRunningBeforePause){ // Pre-Pause
            outState.putBoolean("audioShouldBeRunning", true);
        } else { // Post-Pause
            outState.putBoolean("audioShouldBeRunning", wasAudioRunningBeforePause);
        }
        outState.putInt("mediaStopTime", AudioHandler.mediaStopTime);
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Play Concentration");
        dialog.setMessage("How many tiles?");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView text = new TextView(this);
        SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(8);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text.setText("      " + (4 + i * 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        layout.addView(seekBar);

        text.setText("      4"); // Padded string to make it look nicer
        text.setPadding(10, 10, 10, 10);
        layout.addView(text);

        dialog.setView(layout);
        dialog.setPositiveButton(android.R.string.ok, (d, id) -> {
            Intent i = new Intent(getApplicationContext(), GameActivity.class);
            i.putExtra("tiles", 4 + seekBar.getProgress() * 2);
            startActivity(i);
        });
        dialog.setNegativeButton(android.R.string.cancel, (d, id) -> {
            // Do nothing
        });
        dialog.show();
    }
}