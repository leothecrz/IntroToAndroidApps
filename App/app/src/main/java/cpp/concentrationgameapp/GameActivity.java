package cpp.concentrationgameapp;

import android.animation.ObjectAnimator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, CardFragment.OnCardClickListener {

    // Keep words under 7 letters
    private static final String[] WORDS = {
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
    private LinearLayout[] rows;
    private FragmentContainerView[][] cardContainers;
    private CardFragment[][] cards;
    private TextView scoreDisplay;

    private int tileCount;
    private int score;
    private boolean disableFlip;
    private boolean showedTryAgainToast;
    private CardFragment card1; // First flipped card
    private CardFragment card2; // Second flipped card

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        audioHandler = AudioHandler.getInstance();
        if(!audioHandler.isRunningStatus()){
            audioHandler.play(R.raw.testtrack2, getApplicationContext());
        }

        scoreDisplay = findViewById(R.id.scoreText);
        scoreDisplay.setText(getResources().getString(R.string.score_label, 0));

        // Get buttons
        tryAgainButton = findViewById(R.id.tryAgainButton);
        Button endGameButton = findViewById(R.id.endGameButton);
        Button newGameButton = findViewById(R.id.newGameButton);
        Button toggleSoundButton = findViewById(R.id.toggleSoundButton);
        tryAgainButton.setEnabled(false);

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
                cards[y][x].setPosition(y, x);
            }
        }

        if (savedInstanceState != null) {
            // Restore state from bundle
            tileCount = savedInstanceState.getInt("tileCount");
            setScore(savedInstanceState.getInt("score"));
            disableFlip = savedInstanceState.getBoolean("disableFlip");
            showedTryAgainToast = savedInstanceState.getBoolean("showedTryAgainToast");
            if (savedInstanceState.containsKey("card1")) {
                byte[] card1Pos = savedInstanceState.getByteArray("card1");
                card1 = cards[card1Pos[0]][card1Pos[1]];
            }
            if (savedInstanceState.containsKey("card2")) {
                byte[] card2Pos = savedInstanceState.getByteArray("card2");
                card2 = cards[card2Pos[0]][card2Pos[1]];
            }
            if (disableFlip)
                tryAgainButton.setEnabled(true);
            updateViews();
        } else {
            // Get tile count from intent and initialize the game
            tileCount = getIntent().getIntExtra("tiles", 4);
            initGame();
        }

        // Remove action bar
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("tileCount", tileCount);
        outState.putInt("score", score);
        outState.putBoolean("disableFlip", disableFlip);
        outState.putBoolean("showedTryAgainToast", showedTryAgainToast);
        if (card1 != null)
            outState.putByteArray("card1", new byte[] { (byte)card1.getRow(), (byte)card1.getColumn() });
        if (card2 != null)
            outState.putByteArray("card2", new byte[] { (byte)card2.getRow(), (byte)card2.getColumn() });
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioHandler.stop();
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }

    public void exitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to exit the game?")
                .setPositiveButton(android.R.string.yes, (dialog, button) ->
                        GameActivity.super.onBackPressed())
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tryAgainButton:
                tryAgainButton.setEnabled(false);
                card1.flip();
                card2.flip();
                card1 = null;
                card2 = null;
                disableFlip = false;
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
        if (disableFlip || !fragment.isFlippable())
            return;

        if (card1 == null) {
            // First card was clicked
            card1 = fragment;
        } else if (card2 == null && card1 != fragment) {
            // Second card was clicked
            card2 = fragment;

            // Check for a match
            boolean isMatch = card1.getWord().equals(card2.getWord());
            int textAnimColor; // Used for text color animation
            if (isMatch) {
                setScore(score + 2);
                textAnimColor = 0xFF00FF00;
                card1.setFlippable(false);
                card2.setFlippable(false);
                card1 = null;
                card2 = null;

                if (checkPlayerWon()) {
                    Toast.makeText(this, "You won!", Toast.LENGTH_LONG).show();
                    highScoreDialog();
                }
            } else {
                if (score > 0)
                    setScore(score - 1);
                textAnimColor = 0xFFFF0000;

                // Temporarily prevent player from flipping another card until try again is pressed
                disableFlip = true;
                tryAgainButton.setEnabled(true);
                if (!showedTryAgainToast) {
                    Toast.makeText(this,
                            "Incorrect pair! Press the try again button to try again.",
                            Toast.LENGTH_SHORT).show();
                    showedTryAgainToast = true;
                }
            }

            // Text color animation
            scoreDisplay.setTextColor(textAnimColor);
            ObjectAnimator animator = ObjectAnimator.ofArgb(scoreDisplay, "textColor",
                    textAnimColor, 0xFFFFFFFF);
            animator.setDuration(3000);
            animator.start();
        } else if (card1 == fragment) {
            // First card was clicked again
            card1 = null;
        }

        fragment.flip();
    }

    private void updateViews() {
        // Reset visibility and flip state of cards
        for (LinearLayout row : rows)
            row.setVisibility(View.VISIBLE);
        for (FragmentContainerView[] cardContainers : cardContainers)
            for (FragmentContainerView container : cardContainers)
                container.setVisibility(View.VISIBLE);
        for (CardFragment[] cardFragments : cards)
            for (CardFragment card : cardFragments) {
                card.setFlippable(true);
                if (card.isFlipped())
                    card.flip();
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
    }

    private void initGame() {
        updateViews();
        card1 = null;
        card2 = null;
        disableFlip = false;
        tryAgainButton.setEnabled(false);

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

        // Reset score
        setScore(0);
    }

    private void highScoreDialog() {
        HighScoreTable highScoreTable = HighScoreTable.getInstance();
        if (highScoreTable.isHighScore(tileCount, score)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("High Score");
            dialog.setMessage("You set a high score! Enter your name:");

            // Add text field to dialog
            EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            dialog.setView(input);

            dialog.setPositiveButton(android.R.string.ok, (d, id) -> {
                String name = input.getText().toString();
                boolean res = highScoreTable.addHighScore(name, tileCount, score);
                if (res)
                    Toast.makeText(this, "Added high score!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "Failed to add high score.", Toast.LENGTH_LONG).show();
            });
            dialog.setNegativeButton(android.R.string.cancel, (d, id) -> {});

            dialog.show();
        }
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
                text.setText(getResources().getString(R.string.tile_count_dialog,
                        4 + i * 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        layout.addView(seekBar);

        text.setText(getResources().getString(R.string.tile_count_dialog, 4));
        text.setPadding(10, 10, 10, 10);
        layout.addView(text);

        dialog.setView(layout);
        dialog.setPositiveButton(android.R.string.ok, (d, id) -> {
            tileCount = 4 + seekBar.getProgress() * 2;
            initGame();
        });
        dialog.setNegativeButton(android.R.string.cancel, (d, id) -> {});
        dialog.show();
    }

    private void setScore(int score) {
        this.score = score;
        scoreDisplay.setText(getResources().getString(R.string.score_label, score));
    }

    private boolean checkPlayerWon() {
        // Get list of all visible cards
        List<CardFragment> cardList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (rows[i].getVisibility() == View.GONE)
                continue;
            for (int x = 0; x < 4; x++)
                if (cardContainers[i][x].getVisibility() == View.VISIBLE)
                    cardList.add(cards[i][x]);
        }

        // Flippable card = player has not won
        for (CardFragment card : cardList)
            if (card.isFlippable())
                return false;

        // None of the cards can be flipped; player won
        return true;
    }
}