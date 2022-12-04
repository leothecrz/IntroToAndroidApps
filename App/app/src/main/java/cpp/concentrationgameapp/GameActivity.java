package cpp.concentrationgameapp;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private AudioHandler audioHandler;
    private Button tryAgainButton;
    private Button endGameButton;
    private Button newGameButton;

    private int tileCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        audioHandler = AudioHandler.getInstance();
        if(!audioHandler.isRunningStatus()){
            audioHandler.play(R.raw.testtrack2, getApplicationContext());
        }
        tryAgainButton = findViewById(R.id.tryAgainButton);
        endGameButton = findViewById(R.id.endGameButton);
        newGameButton = findViewById(R.id.newGameButton);

        tryAgainButton.setOnClickListener(this);
        endGameButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);

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
        exitDialog();
    }

    public void exitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to exit the game?")
                .setPositiveButton(android.R.string.yes, (dialog, button) -> {
                    GameActivity.super.onBackPressed();
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tryAgainButton:
                Log.i("cpp_concentration", "tryAgainButton pressed");
                break;
            case R.id.endGameButton:
                exitDialog();
                break;
            case R.id.newGameButton:
                Log.i("cpp_concentration", "newGameButton pressed");
                break;
        }
    }
}