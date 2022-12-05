package cpp.concentrationgameapp;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, CardFragment.OnCardClickListener {

    // Keep words under 7 letters
    private static String[] WORDS = {
            "Camel",
            "Deer",
            "Fox",
            "Horse",
            "Lion",
            "Mouse",
            "Otter",
            "Owl",
            "Tiger",
            "Wolf"
    };

    private AudioHandler audioHandler;
    private Button tryAgainButton;
    private Button endGameButton;
    private Button newGameButton;
    private Button toggleSoundButton;
    private LinearLayout[] rows;
    private FragmentContainerView[][] cardContainers;
    private CardFragment[][] cards;
    private TextView scoreDisplay;

    private int tileCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        audioHandler = AudioHandler.getInstance();
        if(!audioHandler.isRunningStatus()){
            audioHandler.play(R.raw.testtrack2, getApplicationContext());
        }

        scoreDisplay = findViewById(R.id.scoreText);
        scoreDisplay.setText("Score: 0");

        // Get buttons
        tryAgainButton = findViewById(R.id.tryAgainButton);
        endGameButton = findViewById(R.id.endGameButton);
        newGameButton = findViewById(R.id.newGameButton);
        toggleSoundButton = findViewById(R.id.toggleSoundButton);

        // Set button listeners
        tryAgainButton.setOnClickListener(this);
        endGameButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        toggleSoundButton.setOnClickListener(this);

        // Get card rows, containers, and fragments from layout
        rows = new LinearLayout[5];
        cardContainers = new FragmentContainerView[5][4];
        cards = new CardFragment[5][4];
        Resources res = getResources();
        for (int y = 0; y < 5; y++) {
            rows[y] = findViewById(res.getIdentifier("card_row_" + y,"id",
                    getPackageName()));
            for (int x = 0; x < 4; x++) {
                cardContainers[y][x] = findViewById(res.getIdentifier("card_" + y + "x" + x,
                        "id", getPackageName()));
                cards[y][x] = (CardFragment)getSupportFragmentManager().findFragmentById(
                        res.getIdentifier("card_" + y + "x" + x, "id",
                                getPackageName()));
                cards[y][x].setClickListener(this);
            }
        }

        // Get tile count from intent and initialize the game
        tileCount = getIntent().getIntExtra("tiles", 4);
        initGame();

        // Remove action bar
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
                showDialog();
                break;
            case R.id.toggleSoundButton:
                if (!audioHandler.isRunningStatus()) {
                    audioHandler.setEnabled(true);
                    audioHandler.play(R.raw.testtrack2, getApplicationContext());
                } else {
                    audioHandler.setEnabled(false);
                    audioHandler.stop();
                }
                break;
        }
    }

    @Override
    public void onCardClick(CardFragment fragment) {
        fragment.flip();
    }

    public void initGame() {
        // Reset visibility and flip state of cards
        for (LinearLayout row : rows)
            row.setVisibility(View.VISIBLE);
        for (FragmentContainerView[] cardContainers : cardContainers)
            for (FragmentContainerView container : cardContainers)
                container.setVisibility(View.VISIBLE);
        for (CardFragment[] cardFragments : cards) {
            for (CardFragment card : cardFragments) {
                if (card.isFlipped())
                    card.flip();
            }
        }

        // Set visibility of cards based on tile count
        switch (tileCount) {
            case 4:
                // x x
                // x x
                rows[1].setVisibility(View.GONE);
                rows[2].setVisibility(View.GONE);
                rows[3].setVisibility(View.GONE);
                cardContainers[0][1].setVisibility(View.GONE);
                cardContainers[0][2].setVisibility(View.GONE);
                cardContainers[4][1].setVisibility(View.GONE);
                cardContainers[4][2].setVisibility(View.GONE);
                break;
            case 6:
                // x x
                // x x
                // x x
                rows[1].setVisibility(View.GONE);
                rows[3].setVisibility(View.GONE);
                cardContainers[0][1].setVisibility(View.GONE);
                cardContainers[0][2].setVisibility(View.GONE);
                cardContainers[2][1].setVisibility(View.GONE);
                cardContainers[2][2].setVisibility(View.GONE);
                cardContainers[4][1].setVisibility(View.GONE);
                cardContainers[4][2].setVisibility(View.GONE);
                break;
            case 8:
                // x x
                // x x
                // x x
                // x x
                rows[1].setVisibility(View.GONE);
                cardContainers[0][1].setVisibility(View.GONE);
                cardContainers[0][2].setVisibility(View.GONE);
                cardContainers[2][1].setVisibility(View.GONE);
                cardContainers[2][2].setVisibility(View.GONE);
                cardContainers[3][1].setVisibility(View.GONE);
                cardContainers[3][2].setVisibility(View.GONE);
                cardContainers[4][1].setVisibility(View.GONE);
                cardContainers[4][2].setVisibility(View.GONE);
                break;
            case 10:
                // x x
                // x x
                // x x
                // x x
                // x x
                cardContainers[0][1].setVisibility(View.GONE);
                cardContainers[0][2].setVisibility(View.GONE);
                cardContainers[1][1].setVisibility(View.GONE);
                cardContainers[1][2].setVisibility(View.GONE);
                cardContainers[2][1].setVisibility(View.GONE);
                cardContainers[2][2].setVisibility(View.GONE);
                cardContainers[3][1].setVisibility(View.GONE);
                cardContainers[3][2].setVisibility(View.GONE);
                cardContainers[4][1].setVisibility(View.GONE);
                cardContainers[4][2].setVisibility(View.GONE);
                break;
            case 12:
                // x x x
                // x x x
                // x x x
                // x x x
                rows[1].setVisibility(View.GONE);
                cardContainers[0][1].setVisibility(View.GONE);
                cardContainers[2][1].setVisibility(View.GONE);
                cardContainers[3][1].setVisibility(View.GONE);
                cardContainers[4][1].setVisibility(View.GONE);
                break;
            case 14:
                // x x x x
                // x x x x
                // x x x x
                //   x x
                rows[1].setVisibility(View.GONE);
                cardContainers[4][0].setVisibility(View.INVISIBLE);
                cardContainers[4][3].setVisibility(View.INVISIBLE);
                break;
            case 16:
                // x x x x
                // x x x x
                // x x x x
                // x x x x
                rows[1].setVisibility(View.GONE);
                break;
            case 18:
                // x x x x
                // x x x x
                // x x x x
                // x x x x
                //   x x
                cardContainers[4][0].setVisibility(View.INVISIBLE);
                cardContainers[4][3].setVisibility(View.INVISIBLE);
                break;
            case 20:
                // x x x x
                // x x x x
                // x x x x
                // x x x x
                // x x x x
                break;
        }

        // Get list of all visible cards
        List<CardFragment> cardList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (rows[i].getVisibility() == View.GONE)
                continue;
            for (int x = 0; x < 4; x++)
                if (cardContainers[i][x].getVisibility() == View.VISIBLE)
                    cardList.add(cards[i][x]);
        }

        // Shuffle both lists to randomize words used and card pairs
        List<String> wordList = Arrays.asList(WORDS);
        Collections.shuffle(wordList);
        Collections.shuffle(cardList);

        // Set word for each pair of cards
        for (int i = 0; i < cardList.size() / 2; i++) {
            cardList.get(i * 2).setWord(wordList.get(i));
            cardList.get(i * 2 + 1).setWord(wordList.get(i));
        }
    }

    private void showDialog() {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("New Game");
        dialog.setMessage("How many tiles?");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView text = new TextView(this);
        SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(8);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text.setText("      " + (4 + i * 2) + " tiles");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        layout.addView(seekBar);

        text.setText("      4 tiles"); // Padded string to make it look nicer
        text.setPadding(10, 10, 10, 10);
        layout.addView(text);

        dialog.setView(layout);
        dialog.setPositiveButton(android.R.string.ok, (d, id) -> {
            tileCount = 4 + seekBar.getProgress() * 2;
            initGame();
        });
        dialog.setNegativeButton(android.R.string.cancel, (d, id) -> {
            // Do nothing
        });
        dialog.show();
    }
}