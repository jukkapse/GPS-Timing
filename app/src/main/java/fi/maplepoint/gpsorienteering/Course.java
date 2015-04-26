package fi.maplepoint.gpsorienteering;

import android.location.Location;

import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Jukkis on 17.4.2015.
 */
public class Course {
    private int id, cid;
    private String name, length;
    private ArrayList<Control> controls = new ArrayList<>();

    public Course(Integer id) {
        this.id = id;
        controls = new ArrayList<Control>();
    }
    public void addControls(ArrayList<Control> controls) {
        this.controls = controls;
    }
    public ArrayList<Control> getControls(){
        return this.controls;
    }

    public void setCompetitionID(Integer id){
        this.cid = id;
    }
    public Integer getCompetitionID(){
        return this.cid;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public int getID(){
        return this.id;
    }

    public void setLength(String length){
        this.length = length;
    }
    public String getLength(){
        return this.length;
    }
}
