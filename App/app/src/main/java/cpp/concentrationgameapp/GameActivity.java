package cpp.concentrationgameapp;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;

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
        toggleSoundButton = findViewById(R.id.toggleSoundButton);

        tryAgainButton.setOnClickListener(this);
        endGameButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        toggleSoundButton.setOnClickListener(this);

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

        tileCount = getIntent().getIntExtra("tiles", 4);
        initGame();

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
        for (LinearLayout row : rows)
            row.setVisibility(View.VISIBLE);
        for (FragmentContainerView[] cardContainers : cardContainers)
            for (FragmentContainerView container : cardContainers)
                container.setVisibility(View.VISIBLE);

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
}