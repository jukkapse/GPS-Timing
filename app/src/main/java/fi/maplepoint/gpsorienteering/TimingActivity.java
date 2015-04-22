package fi.maplepoint.gpsorienteering;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Timing activity takes care of active_view.
 * Created by Jukkis on 18.4.2015.
 */
public class TimingActivity extends Activity {
    Chronometer chronometer;
    Button startButton;
    LocationManager locationManager;
    LocationListener locationListener;
    MediaPlayer controlBeep, startBeep;
    TextView firstnameText, lastnameText, courseText, clubText, distanceText, speed;
    ArrayList<Location> controls;
    ArrayList<Double> controlTimes;
    Double travelledDistance;
    String course;
    String[] runner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_view);
        runner = getIntent().getStringArrayExtra("Runner");

        course = "LongaNatten";

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        startButton = (Button) findViewById(R.id.startbutton);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        controlBeep = MediaPlayer.create(TimingActivity.this, R.raw.controlbeep);
        startBeep = MediaPlayer.create(TimingActivity.this, R.raw.startbeep);

        courseText = (TextView) findViewById(R.id.course);
        firstnameText = (TextView) findViewById(R.id.firstname);
        lastnameText = (TextView) findViewById(R.id.lastname);
        clubText = (TextView) findViewById(R.id.clubtext);
        distanceText = (TextView) findViewById(R.id.distance);
        speed = (TextView) findViewById(R.id.speed);

        courseText.setText(course);
        firstnameText.setText(runner[1]);
        lastnameText.setText(runner[2]);
        clubText.setText(runner[3]);
        travelledDistance = 0.0;

        controls = new ArrayList<>();
        controlTimes = new ArrayList<>();
        Location control = new Location("2");
        control.setLatitude(60.166775);
        control.setLongitude(24.798710);
        Location control2 = new Location("2");
        control2.setLatitude(60.163148);
        control2.setLongitude(24.806030);
        Location control3 = new Location("2");
        control3.setLatitude(60.160324);
        control3.setLongitude(24.802151);
        Location control4 = new Location("2");
        control4.setLatitude(60.159624);
        control4.setLongitude(24.794984);
        Location control5 = new Location("2");
        control5.setLatitude(60.162700);
        control5.setLongitude(24.793379);
        Location control6 = new Location("2");
        control6.setLatitude(60.165137);
        control6.setLongitude(24.792941);
        Location control7 = new Location("2");
        control7.setLatitude(60.167450);
        control7.setLongitude(24.797156);


        controls.add(control);
        controls.add(control2);
        controls.add(control3);
        controls.add(control4);
        controls.add(control5);
        controls.add(control6);
        controls.add(control7);

        distanceText.setText(getDistance(controls) + " km\n" + controls.size() + " Controls");


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startButton.setVisibility(View.VISIBLE);

        } else {
            startButton.setVisibility(View.INVISIBLE);
            showGPSDisabledAlertToUser();
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                checkControl(location, controls.get(0));

                //Show speed min/km
                speed.setText("" + String.format("%.2f", (16.666666667 / location.getSpeed())) + " min/km");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setEnabled(false);
                MainActivity.stopBackgroundMusic();
                new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        if (millisUntilFinished / 1000 <= 5) {
                            controlBeep.start();
                        }
                        startButton.setText("" + millisUntilFinished / 1000);

                    }

                    public void onFinish() {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        startBeep.start();
                        startButton.setText("Go!");
                        startButton.setVisibility(View.INVISIBLE);

                    }
                }.start();
            }
        });

    }

    //Show notification if GPS is disabled
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.gpshelper))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.enablegps),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void checkControl(Location gps, Location control) {
        //Get GPS latitude and longnitude
        Double lat = gps.getLatitude();
        Double lon = gps.getLongitude();

        //Get control latitude and longnitude
        Double cLat = control.getLatitude();
        Double cLon = control.getLongitude();

        //Radius around control where "punching" is allowed + GPS Accuracy!
        Double rad = 0.00010;
//                + (gps.getAccuracy() * Math.pow(10, -7));

        //Check if GPS location is inside control radius
        if ((Math.pow((lat - cLat), 2) + Math.pow((lon - cLon), 2)) < (Math.pow(rad, 2))) {
            //Punch control
            Double controltime;
            if (controlTimes.isEmpty()) {
                controltime = (SystemClock.elapsedRealtime() - chronometer.getBase() + 0.0);
            } else {
                controltime = (SystemClock.elapsedRealtime() - controlTimes.get(controlTimes.size() - 1));
            }
            controlTimes.add(controltime);
            //Next send controltime to the server


            //Remove punched control from controls list
            controls.remove(0);

            //Punching signals
            controlBeep.start();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);

            //When no controls left -> Stop Chronometer and GPS
            if (controls.isEmpty()) {
                chronometer.stop();
                locationManager.removeUpdates(locationListener);
            }
        }
    }

    //Calculate route distance and return it as 2 decimal String.
    public String getDistance(ArrayList<Location> controls) {
        Double distance = 0.0;
        double d2r = Math.PI / 180;
        if (controls.size() > 1) {
            for (int i = 0; i < controls.size() - 1; i++) {
                double dlong = (controls.get(i + 1).getLongitude() - controls.get(i).getLongitude()) * d2r;
                double dlat = (controls.get(i + 1).getLatitude() - controls.get(i).getLatitude()) * d2r;
                double a = Math.pow(Math.sin(dlat / 2.0), 2)
                        + Math.cos(controls.get(i).getLatitude() * d2r)
                        * Math.cos(controls.get(i + 1).getLatitude() * d2r)
                        * Math.pow(Math.sin(dlong / 2.0), 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                distance += 6367 * c;
            }
        }
        return String.format("%.2f", distance);
    }
}
