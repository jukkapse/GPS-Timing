package fi.maplepoint.gpsorienteering;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Jukkis on 17.4.2015.
 */
public class Course {
    private int id;
    private String name;
    private double length;
    private ArrayList<Location> controls = new ArrayList<>();

    public Course(Integer competitionID) {

        setControls();
    }

    public void setControls() {
        Location control = new Location("" + controls.size() + 1);
        control.setLatitude(2.1);
        control.setLongitude(2.1);
        controls.add(control);
    }
}
