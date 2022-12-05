package cpp.concentrationgameapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class HighScoreActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner tileDropdown;
    private TextView highScore1;
    private TextView highScore2;
    private TextView highScore3;
    private AudioHandler audioHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        audioHandler = AudioHandler.getInstance();



        tileDropdown = findViewById(R.id.tile_dropdown); // Tile dropdown menu
        ArrayAdapter<String> tileAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                new String[] { "4 tiles", "6 tiles", "8 tiles", "10 tiles", "12 tiles", "14 tiles",
                        "16 tiles", "18 tiles", "20 tiles"
                }); // Tile dropdown items
        tileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tileDropdown.setAdapter(tileAdapter);
        tileDropdown.setOnItemSelectedListener(this);
        Button backButton = findViewById(R.id.backButton);
        Button resetButton = findViewById(R.id.resetButton);
        backButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        highScore1 = findViewById(R.id.high_score_1);
        highScore2 = findViewById(R.id.high_score_2);
        highScore3 = findViewById(R.id.high_score_3);

        // Set action bar title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            //actionBar.setTitle("High Scores");
            actionBar.hide();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(audioHandler.isRunningStatus()){
            audioHandler.stop();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if ( getIntent().getBooleanExtra("musicPlaying", false) ){
            audioHandler.play(R.raw.testtrack3, getApplicationContext());
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        updateTextViews(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }

    private TextView getHighScoreView(int i) {
        switch (i) {
            case 0:
                return highScore1;
            case 1:
                return highScore2;
            case 2:
                return highScore3;
            default:
                return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.resetButton:
                resetDialog();
                break;
        }
    }

    private void updateTextViews(int i) {
        HighScore[] scores = HighScoreTable.getInstance().getHighScores(i, false);

        if (scores[0] == null) {
            // No high scores for the given tile count
            highScore1.setText(R.string.no_high_scores);
            highScore2.setVisibility(View.GONE);
            highScore3.setVisibility(View.GONE);
            return;
        }

        // Set text for each score TextView until it reaches an empty score
        for (int x = 0; x < scores.length; x++) {
            HighScore score = scores[x];
            TextView scoreView = getHighScoreView(x);
            if (scoreView == null)
                return;
            scoreView.setVisibility(View.VISIBLE);
            scoreView.setText(getResources().getString(R.string.high_score,
                    x + 1, score != null
                            ? (score.getName() + " - " + score.getScore())
                            : "[empty]"));
        }
    }

    private void resetDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Reset scores");
        dialog.setMessage("Are you sure you want to reset all high scores?");

        dialog.setPositiveButton(android.R.string.yes, (d, id) -> {
            HighScoreTable.getInstance().clear();
            updateTextViews(tileDropdown.getSelectedItemPosition());
        });
        dialog.setNegativeButton(android.R.string.no, (d, id) -> {});

        dialog.show();
    }
}