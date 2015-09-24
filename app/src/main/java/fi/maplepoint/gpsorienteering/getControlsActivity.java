package fi.maplepoint.gpsorienteering;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Jukkis on 25.4.2015.
 */
public class getControlsActivity extends AsyncTask<String, Void, ArrayList<Control>> {

    private Context context;

    public getControlsActivity(Context context) {
        this.context = context;
    }
    @Override
    protected ArrayList<Control> doInBackground(String... arg0) {
        try {
            String id = arg0[0];
            String link = "http://outdoorathletics.fi/gps-timing/mobilelibs/load_controls.php";
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
            ArrayList<Control> controls = new ArrayList<>();
            Control control = new Control("999");
            Integer i = 0;
            while ((line = reader.readLine()) != null) {
                if (i == 0) {
                    control = new Control(line);
                    control.setCourseID(arg0[0]);
                } else if (i == 1) {
                    control.setCnum(line);
                } else if (i == 2) {
                    control.setLongitude(line);

                } else {
                    control.setLatitude(line);
                    controls.add(control);
                    i = -1;
                }
                i++;
            }
            return controls;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    @Override
    protected void onPostExecute(ArrayList<Control> controls) {
    }
}
