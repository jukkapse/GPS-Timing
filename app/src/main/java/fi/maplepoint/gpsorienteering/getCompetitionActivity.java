package fi.maplepoint.gpsorienteering;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Jukkis on 21.4.2015.
 */
public class getCompetitionActivity extends AsyncTask<String, Void, ArrayList<String[]>> {

    private ProgressDialog dialog;
    private Context context;

    public getCompetitionActivity(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(true);
        dialog.setMessage("Loading events...");
        dialog.show();
    }

    @Override
    protected ArrayList<String[]> doInBackground(String... arg0) {
        try {
            String lat = arg0[0];
            String lon = arg0[1];
            String link = "http://outdoorathletics.fi/gps-timing/libs/load_comps.php";
            String data = URLEncoder.encode("lat", "UTF-8")
                    + "=" + URLEncoder.encode(lat, "UTF-8");
            data += "&" + URLEncoder.encode("lon", "UTF-8")
                    + "=" + URLEncoder.encode(lon, "UTF-8");
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

            String[] competition = new String[10];
            // competitions.add(competition);
            Integer i = 0;
            while ((line = reader.readLine()) != null) {
                competition[i] = line;
                if (i % 2 == 0 && i != 0) {
                    competitions.add(competition);
                    competition = new String[10];
                    i=-1;
                }
                i++;
            }
            return competitions;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    @Override
    protected void onPostExecute(ArrayList<String[]> competitions) {
        dialog.cancel();
    }
}
