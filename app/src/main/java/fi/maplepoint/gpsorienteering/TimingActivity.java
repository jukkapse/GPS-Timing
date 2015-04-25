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
import java.util.UUID;

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
    ArrayList<Integer> legTimes;
    ArrayList<Double[]> courseData;
    String course, uuid;
    String[] runner;
    Boolean timing;
    Integer cid, cnum, controlTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_view);

        runner = getIntent().getStringArrayExtra("Runner");

        course = "LongaNatten";

        controls = new ArrayList<>();
        legTimes = new ArrayList<>();

        cid = 1;
        cnum = 1;
        timing = false;
        controlTime = 0;
        uuid = UUID.randomUUID().toString();

        //Build course
//        courseData = getCourseActivity(cid);
//        for (int i = 0; i < courseData.size(); i++) {
//            Location control = new Location("");
//            control.setLatitude(courseData.get(i)[0]);
//            control.setLongitude(courseData.get(i)[1]);
//            controls.add(control);
//        }

        //Chronometer
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        //StartButton
        startButton = (Button) findViewById(R.id.startbutton);
        startButton.setVisibility(View.INVISIBLE);

        //Sounds
        startBeep = MediaPlayer.create(TimingActivity.this, R.raw.startbeep);
        controlBeep = MediaPlayer.create(TimingActivity.this, R.raw.controlbeep);

        //GPS location manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //TextViews
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

        Location control = new Location("2");
        control.setLatitude(60.167153);
        control.setLongitude(24.797313);
        Location control2 = new Location("2");
        control2.setLatitude(60.167024);
        control2.setLongitude(24.798407);
        Location control3 = new Location("2");
        control3.setLatitude(60.167236);
        control3.setLongitude(24.798479);
        Location control4 = new Location("2");
        control4.setLatitude(60.167485);
        control4.setLongitude(24.797647);

        controls.add(control);
        controls.add(control2);
        controls.add(control3);
        controls.add(control4);

        //Show course distance
        distanceText.setText(getDistance(controls) + " km\n" + (controls.size() - 1) + " Controls");

        //Check if GPS is enabled!
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //Show GPS Accuracy
                courseText.setText("Accuracy: " + location.getAccuracy() + "m");

                //Show starting button if standing at starting place and GPS accuracy is better than 20m.
                if (!timing && insideArea(location, controls.get(0))) {
                    if (location.getAccuracy() > 20) {
                        startButton.setVisibility(View.INVISIBLE);
                    } else {
                        startButton.setVisibility(View.VISIBLE);
                    }
                }

                //Check if location is inside control area
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

                //Remove starting point
                controls.remove(0);

                //Disable StartButton
                startButton.setEnabled(false);

                //Stop BackgroundMusic
                MainActivity.stopBackgroundMusic();

                //Start CounDownTimer (10s)
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
                        timing = true;
                        startBeep.start();
                        startButton.setVisibility(View.INVISIBLE);
                    }
                }.start();
            }
        });
    }

    //Check if current position is inside control area
    private boolean insideArea(Location gps, Location control) {
        //Get GPS latitude and longnitude
        Double lat = gps.getLatitude();
        Double lon = gps.getLongitude();

        //Get control latitude and longnitude
        Double cLat = control.getLatitude();
        Double cLon = control.getLongitude();

        //Radius around control where "punching" is allowed + GPS Accuracy!
        Double rad = 0.00005
                + (gps.getAccuracy() * Math.pow(10, -5));

        if ((Math.pow((lat - cLat), 2) + Math.pow((lon - cLon), 2)) < (Math.pow(rad, 2))) {
            return true;
        } else {
            return false;
        }

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

    //Punch control if location is inside control area
    public void checkControl(Location gps, Location control) {

        //Check if GPS location is insideArea control radius
        if (insideArea(gps, control)) {

            //Punch control if timing is running
            int legTime = 0;
            if (timing) {
                if (legTimes.isEmpty()) {
                    legTime = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);
                } else {
                    legTime = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 - controlTime);
                }
                legTimes.add(legTime);
                controlTime += legTime;

                //Punching signals
                controlBeep.start();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(1000);
                courseText.setText(Integer.toString(legTime));

                //Next send legTime to the server
                new controlTimingActivity(courseText).execute(Integer.toString(cid), runner[0], Integer.toString(cnum), Integer.toString(legTime),Integer.toString(controlTime), uuid);

                //Remove punched control from controls list
                controls.remove(0);
                cnum++;

                //When no controls left -> Stop Chronometer and GPS
                if (controls.isEmpty()) {
                    chronometer.stop();
                    timing = false;
                    locationManager.removeUpdates(locationListener);
                }
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
