package fi.maplepoint.gpsorienteering;

import android.location.Location;
import android.location.LocationListener;

import java.util.ArrayList;

/**
 * Created by Jukkis on 17.4.2015.
 */
public class Competition {
    private Integer id;
    private String name, location;
    private ArrayList<Course> courses = new ArrayList<>();
    private Location start;

    public Competition(Integer id) {
        this.id = id;
        this.start = new Location("");
    }
    public Integer getID(){
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void addCourses(ArrayList<Course> courses){
        this.courses = courses;
    }
    public ArrayList<Course> getCourses(){
        return this.courses;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation(){
        return this.location;
    }

    public void setStart(String lat, String lon) {
        start.setLatitude(Double.parseDouble(lat));
        start.setLongitude(Double.parseDouble(lon));
    }
    public Location getStart(){
        return this.start;
    }
}
