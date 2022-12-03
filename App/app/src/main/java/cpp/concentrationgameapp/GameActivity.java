package cpp.concentrationgameapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GameActivity extends AppCompatActivity {

    private AudioHandler audioHandler;
    private TableLayout cardGrid;
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

        cardGrid = findViewById(R.id.cardTable);
        for (int i = 0; i < 4; i++) {
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER);
            row.setId(7000 + i);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));

            for (int x = 0; x < 4; x++) {
                CardFragment cardFragment = CardFragment.newCard("Card " + i + " " + x);
                getFragmentManager().beginTransaction()
                        .add(row.getId(), cardFragment, null)
                        .commit();
            }

            cardGrid.addView(row);
        }

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
}