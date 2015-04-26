package fi.maplepoint.gpsorienteering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

public class selectCourseActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    static List<String> listDataHeader;
    static HashMap<String, ArrayList<Course>> listDataChild;
    LocationManager locationManager;
    LocationListener locationListener;
    public ArrayList<Competition> competitions;
    boolean locationFix = false;
    private ProgressDialog waitingGPS;
    private String[] runnerData;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_course);
        context = this;

        runnerData = getIntent().getStringArrayExtra("Runner");
        final Runner runner = new Runner(runnerData[0], runnerData[1], runnerData[2], runnerData[3]);

        waitingGPS = new ProgressDialog(this);
        waitingGPS.setCancelable(true);
        waitingGPS.setMessage("Waiting for GPS-signal...");
        waitingGPS.show();


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() < 70) {
                    try {
                        competitions = new getCompetitionActivity(context).execute(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    locationFix = true;
                    waitingGPS.cancel();

                    locationManager.removeUpdates(locationListener);
                    for (int i = 0; i < competitions.size(); i++) {
                        try {
                            competitions.get(i).addCourses(new getCourseActivity(context).execute(Integer.toString(competitions.get(i).getID())).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    if (locationFix) {
                        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

                        // preparing list data
                        prepareListData();

                        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);

                        // setting list adapter
                        expListView.setAdapter(listAdapter);

                        // Listview on child click listener
                        expListView.setOnChildClickListener(new OnChildClickListener() {

                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v,
                                                        int groupPosition, int childPosition, long id) {
                                Intent i = new Intent(context, TimingActivity.class);
                                i.putExtra("courseID",Integer.toString(listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition).getID()));
                                i.putExtra("courseText", listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition).getText());
                                i.putExtra("compName", listDataHeader.get(groupPosition));
                                i.putExtra("runner", runner);
                                startActivity(i);

                                return false;
                            }
                        });
                    }
                }
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
        // get the listview
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        ArrayList<Course> courses;
        for (int i = 0; i < competitions.size(); i++) {
            try {
                competitions.get(i).addCourses(new getCourseActivity(context).execute(Integer.toString(competitions.get(i).getID())).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            listDataHeader.add(competitions.get(i).getName());
            listDataChild.put(listDataHeader.get(i), competitions.get(i).getCourses());
        }
    }
}