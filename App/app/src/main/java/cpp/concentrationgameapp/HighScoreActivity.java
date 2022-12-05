package cpp.concentrationgameapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

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
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        highScore1 = findViewById(R.id.high_score_1);
        highScore2 = findViewById(R.id.high_score_2);
        highScore3 = findViewById(R.id.high_score_3);

        // Set action bar title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("High Scores");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        HighScore[] scores = HighScoreTable.getInstance().getHighScores(i, false);

        if (scores[0] == null) {
            // No high scores for the given tile count
            highScore1.setText("No high scores.");
            highScore2.setVisibility(View.GONE);
            highScore3.setVisibility(View.GONE);
            return;
        }

        // Set text for each score TextView until it reaches an empty score
        for (int x = 0; x < scores.length; x++) {
            HighScore score = scores[x];
            TextView scoreView = getHighScoreView(x);
            scoreView.setVisibility(View.VISIBLE);
            scoreView.setText((x + 1) + ". " + (score != null
                    ? (score.getName() + " - " + score.getScore())
                    : "[empty]"));
        }
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
        finish();
    }
}