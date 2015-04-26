package fi.maplepoint.gpsorienteering;

import android.location.Location;

/**
 * Created by Jukkis on 25.4.2015.
 */
public class Control {
    Integer id,courseID, cnum;
    String latitude, longitude;

    public Control(String id){
        this.id = Integer.parseInt(id);
    }
    public void setCourseID(String courseID){
        this.courseID = Integer.parseInt(courseID);
    }
    public Integer getCourseID(){
        return this.courseID;
    }
    public void setCnum(String cnum){
        this.cnum = Integer.parseInt(cnum);
    }
    public Integer getCnum(){
        return this.cnum;
    }
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }
    public String getLatitude(){
        return this.latitude;
    }
    public void setLongitude(String longitude){
        this.longitude = longitude;
    }
    public String getLongitude(){
        return this.longitude;
    }
    public Location getLocation(){
        Location control = new Location("");
        control.setLatitude(Double.parseDouble(this.latitude));
        control.setLongitude(Double.parseDouble(this.longitude));
        return control;
    }
}
