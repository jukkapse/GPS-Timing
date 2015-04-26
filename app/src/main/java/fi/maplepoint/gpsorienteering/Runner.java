package fi.maplepoint.gpsorienteering;

import java.io.Serializable;

/**
 * Created by Jukkis on 26.4.2015.
 */
public class Runner implements Serializable {
    String id, firstname, lastname, club;
    public Runner(String id, String firstname, String lastname, String club) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.club = club;
    }
    public String getID(){
        return this.id;
    }
    public String getFirstname(){
        return this.firstname;
    }
    public String getLastname(){
        return this.lastname;
    }
    public String getClub(){
        return this.club;
    }
}
