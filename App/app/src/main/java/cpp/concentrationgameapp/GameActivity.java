package cpp.concentrationgameapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    private AudioHandler audioHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        boolean audioShouldBePlaying = getIntent().getBooleanExtra(HomeActivity.audioIntentDataName, true); // Music Plays By Default
        audioHandler = new AudioHandler();
        if(audioShouldBePlaying){
            audioHandler.play(getApplicationContext());
        } else {
            audioHandler.stop();
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        audioHandler.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}