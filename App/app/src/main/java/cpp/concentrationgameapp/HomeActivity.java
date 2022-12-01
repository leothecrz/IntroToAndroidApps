package cpp.concentrationgameapp;

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

                    break;
                }
                default:
                    System.out.println( view );
                    Log.e("homeListener Failure: ", view.toString());
                    break;

            }
        };
        homeExitButton.setOnClickListener(homeListener);


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}