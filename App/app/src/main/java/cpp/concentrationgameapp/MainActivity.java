package cpp.concentrationgameapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


        new Handler().postDelayed(
            () -> {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                this.finish();
            }, 3000
        );



    }

}