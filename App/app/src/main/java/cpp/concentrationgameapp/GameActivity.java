package cpp.concentrationgameapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GameActivity extends AppCompatActivity {

    private AudioHandler audioHandler;

    private int tileCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        audioHandler = AudioHandler.getInstance();
        if(!audioHandler.isRunningStatus()){
            audioHandler.play(R.raw.testtrack2, getApplicationContext());
        }

        tileCount = getIntent().getIntExtra("tiles", 4);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioHandler.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("Exit Game")
            .setMessage("Are you sure you want to exit the game?")
            .setPositiveButton(android.R.string.yes, (dialog, button) -> {
                GameActivity.super.onBackPressed();
            })
            .setNegativeButton(android.R.string.no, null).show();
    }
}