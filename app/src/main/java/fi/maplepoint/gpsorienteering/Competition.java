package fi.maplepoint.gpsorienteering;

import java.util.ArrayList;

/**
 * Created by Jukkis on 17.4.2015.
 */
public class Competition {
    private Integer id;
    private String name;
    private String organizer;
    private ArrayList<Course> courses = new ArrayList<>();

    public Competition(Integer id) {
        this.id = id;
    }
}
