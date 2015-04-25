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
public class getCourseActivity extends AsyncTask<String, Void, List<String>> {


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
    protected List<String> doInBackground(String... arg0) {
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
            ArrayList<String[]> competitions = new ArrayList<>();

            List<String> courses = new ArrayList<String>();
            Integer i = 0;
            while ((line = reader.readLine()) != null) {
                courses.add(line);
            }
            return courses;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    @Override
    protected void onPostExecute(List<String> competitions) {
        dialog.cancel();
    }
}
