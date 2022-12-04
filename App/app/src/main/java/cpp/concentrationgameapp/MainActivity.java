package cpp.concentrationgameapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.Executor;

/**
 * Entry Point Into Program
 * Splash is unnecessary but potential to increase the app's looks
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View systemView = getWindow().getDecorView();
        systemView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //Remove ActionBar
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        // Initialize high score table
        HighScoreTable.getInstance().init(getApplicationContext());

        //Continue to next Activity: HomeActivity.

        //Memory Leak?? - Splash Not Required
        new Handler().postDelayed(
            () -> {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                this.finish();
            }, 3000
        );
    }

}