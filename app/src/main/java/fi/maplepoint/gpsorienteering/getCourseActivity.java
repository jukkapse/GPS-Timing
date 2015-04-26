package fi.maplepoint.gpsorienteering;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jukkis on 21.4.2015.
 */
public class getCourseActivity extends AsyncTask<String, Void, ArrayList<Course>> {


    private ProgressDialog dialog;
    private Context context;

    public getCourseActivity(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(true);
        dialog.setMessage("Loading courses...");
        dialog.show();
    }

    @Override
    protected ArrayList<Course> doInBackground(String... arg0) {
        try {
            String id = arg0[0];
            String link = "http://outdoorathletics.fi/gps-timing/libs/load_courses.php";
            String data = URLEncoder.encode("id", "UTF-8")
                    + "=" + URLEncoder.encode(id, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(conn.getInputStream()));
            String line;
            // Read Server Response

            ArrayList<Course> courses = new ArrayList<>();
            Course course = new Course(null);
            Integer i = 0;
            while ((line = reader.readLine()) != null) {
                if (i == 0) {
                    course = new Course(Integer.parseInt(line));
                } else if (i == 1) {
                    course.setName(line);
                } else if (i == 2) {
                    course.setCompetitionID(Integer.parseInt(line));
                } else {
                    course.setLength(line);
                    courses.add(course);
                    i = -1;
                }
                i++;
            }
            return courses;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Course> competitions) {
        dialog.cancel();
    }
}
