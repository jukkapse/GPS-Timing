package fi.maplepoint.gpsorienteering;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jukkis on 22.4.2015.
 */
public class selectCourseActivity extends ListActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_course);
    }
}
