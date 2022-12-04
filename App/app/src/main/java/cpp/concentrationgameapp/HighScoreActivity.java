package cpp.concentrationgameapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class HighScoreActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner tileDropdown;
    private TextView highScore1;
    private TextView highScore2;
    private TextView highScore3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        tileDropdown = findViewById(R.id.tile_dropdown);
        ArrayAdapter<String> tileAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                new String[] { "4 tiles", "6 tiles", "8 tiles", "10 tiles", "12 tiles", "14 tiles",
                        "16 tiles", "18 tiles", "20 tiles"
                });
        tileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tileDropdown.setAdapter(tileAdapter);
        tileDropdown.setOnItemSelectedListener(this);

        highScore1 = findViewById(R.id.high_score_1);
        highScore2 = findViewById(R.id.high_score_2);
        highScore3 = findViewById(R.id.high_score_3);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("High Scores");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        HighScore[] scores = HighScoreTable.getInstance().getHighScores(i, false);
        if (scores[0] == null) {
            highScore1.setText("No high scores.");
            highScore2.setVisibility(View.GONE);
            highScore3.setVisibility(View.GONE);
            return;
        }

        for (int x = 0; x < scores.length; x++) {
            HighScore score = scores[x];
            if (score == null)
                break;
            TextView scoreView = getHighScoreView(x);
            scoreView.setVisibility(View.VISIBLE);
            scoreView.setText((x + 1) + ". " + score.getName() + " - " + score.getScore());
        }

        // Don't show empty scores
        for (int x = 0; x < scores.length; x++) {
            HighScore score = scores[x];
            if (score == null) {
                getHighScoreView(x).setVisibility(View.GONE);;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
}