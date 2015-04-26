package fi.maplepoint.gpsorienteering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
    static HashMap<String, List<String>> listDataChild;
    LocationManager locationManager;
    LocationListener locationListener;
    public ArrayList<Competition> competitions;
    boolean locationFix = false;
    private ProgressDialog waitingGPS;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_course);
        context = this;

        waitingGPS = new ProgressDialog(this);
        waitingGPS.setCancelable(true);
        waitingGPS.setMessage("Waiting for GPS-signal...");
        waitingGPS.show();


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() < 70) {
                    waitingGPS.cancel();
                    try {
                        competitions = new getCompetitionActivity(context).execute(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    locationFix = true;
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

                        // Listview Group click listener
                        expListView.setOnGroupClickListener(new OnGroupClickListener() {

                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v,
                                                        int groupPosition, long id) {
                                // Toast.makeText(getApplicationContext(),
                                // "Group Clicked " + listDataHeader.get(groupPosition),
                                // Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });

                        // Listview Group expanded listener
                        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

                            @Override
                            public void onGroupExpand(int groupPosition) {
                                Toast.makeText(getApplicationContext(),
                                        listDataHeader.get(groupPosition) + " Expanded",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Listview Group collasped listener
                        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

                            @Override
                            public void onGroupCollapse(int groupPosition) {
                                Toast.makeText(getApplicationContext(),
                                        listDataHeader.get(groupPosition) + " Collapsed",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });

                        // Listview on child click listener
                        expListView.setOnChildClickListener(new OnChildClickListener() {

                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v,
                                                        int groupPosition, int childPosition, long id) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        listDataHeader.get(groupPosition)
                                                + " : "
                                                + listDataChild.get(
                                                listDataHeader.get(groupPosition)).get(
                                                childPosition), Toast.LENGTH_SHORT)
                                        .show();
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
        List<String> courses = new ArrayList<>();
        for (int i = 0; i < competitions.size(); i++) {
            try {
                competitions.get(i).addCourses(new getCourseActivity(context).execute(Integer.toString(competitions.get(i).getID())).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            listDataHeader.add(competitions.get(i).getName());
            for (int j = 0; j < competitions.get(i).getCourses().size(); j++) {
                courses = new ArrayList<>();
                courses.add(competitions.get(i).getCourses().get(j).getName());
            }
            listDataChild.put(listDataHeader.get(i), courses);
        }


        // Adding child data
//        List<String> top250 = new ArrayList<String>();
//        top250.add("The Shawshank Redemption");
//        top250.add("The Godfather");
////        top250.add("The Godfather: Part II");
////        top250.add("Pulp Fiction");
////        top250.add("The Good, the Bad and the Ugly");
////        top250.add("The Dark Knight");
////        top250.add("12 Angry Men");
////
//        List<String> nowShowing = new ArrayList<String>();
//        nowShowing.add("The Conjuring");
////        nowShowing.add("Despicable Me 2");
////        nowShowing.add("Turbo");
////        nowShowing.add("Grown Ups 2");
////        nowShowing.add("Red 2");
////        nowShowing.add("The Wolverine");
//
//        List<String> comingSoon = new ArrayList<String>();
//        comingSoon.add("Pitkämatka 12,2 km / 13 rastia");
//        comingSoon.add("Keskimatka 2,2 km / 20 rastia");
////
//       listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}