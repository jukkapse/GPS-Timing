package fi.maplepoint.gpsorienteering;

/**
 * Created by Jukkis on 25.4.2015.
 */
public class Control {
    Integer id,courseID, cnum;
    String latitude, longitude;

    public Control(Integer id){
        this.id = id;
    }
    public void setCourseID(Integer courseID){
        this.courseID = courseID;
    }
    public Integer getCourseID(){
        return this.courseID;
    }
    public void setCnum(Integer cnum){
        this.cnum = cnum;
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
}
