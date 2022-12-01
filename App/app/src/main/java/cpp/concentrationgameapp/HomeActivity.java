package cpp.concentrationgameapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View.OnClickListener;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        Button homePlayButton = findViewById(R.id.homePlayButton);
        Button homeSettingsButton = findViewById(R.id.homeSettingsButton);
        Button homeExitButton = findViewById(R.id.homeExitButton);
        Button homeHighScoresButton = findViewById(R.id.homeHighScoresButton);

        OnClickListener homeListener = view -> {
            switch (view.getId()){
                case (R.id.homeExitButton):{
                    this.finish();
                    break;
                }
                case (R.id.homePlayButton):{

                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(i);
                    break;
                }
                default:
                    System.out.println( view );
                    Log.e("homeListener Failure: ", view.toString());
                    break;

            }
        };
        homeExitButton.setOnClickListener(homeListener);
        homePlayButton.setOnClickListener(homeListener);

        homeSettingsButton.setOnClickListener(homeListener);
        homeHighScoresButton.setOnClickListener(homeListener);


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}