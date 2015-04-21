package fi.maplepoint.gpsorienteering;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

/**
 * Created by Jukkis on 18.4.2015.
 */
public class settingsActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setttings_view);
    }
}
