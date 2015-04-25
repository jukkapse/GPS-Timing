package fi.maplepoint.gpsorienteering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private EditText usernameField, passwordField;
    public ArrayList<Location> controls = new ArrayList<>();
    public static MediaPlayer mediaPlayer;
    Context context;

    TextView status;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        // Create new fullscreen login view
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login);
        context = this;

        usernameField = (EditText) findViewById(R.id.runnerName);
        passwordField = (EditText) findViewById(R.id.password);

        //Some background music
        playBackgroundMusic();

        //Initialize login fields
        final Button power = (Button) findViewById(R.id.power);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        status = (TextView) findViewById(R.id.invalid);

        //Login with credentials
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                new SigninActivity(context, status).execute(username, password);
            }
        });

        //Turn off application
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    private void playBackgroundMusic() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.forestsounds);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        mediaPlayer.setVolume(1.0f, 1.0f);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        playBackgroundMusic();
        usernameField.setText("");
        passwordField.setText("");
        status.setText("");
    }


    public static void stopBackgroundMusic() {
        mediaPlayer.release();
    }
}
