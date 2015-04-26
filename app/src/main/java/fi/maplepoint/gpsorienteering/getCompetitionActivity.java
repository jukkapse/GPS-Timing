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
public class getCompetitionActivity extends AsyncTask<String, Void, ArrayList<Competition>> {

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
    protected ArrayList<Competition> doInBackground(String... arg0) {
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
            ArrayList<Competition> competitions = new ArrayList<>();
            String latitude = "";
            String longnitude = "";

            Competition competition = new Competition(null);
            Integer i = 0;
            while ((line = reader.readLine()) != null) {
                if(i==0){
                    competition = new Competition(Integer.parseInt(line));
                }else if(i==1){
                    competition.setName(line);
                } else if (i==2) {
                    latitude = line;
                } else if (i==3){
                    longnitude = line;
                    competition.setStart(latitude, longnitude);
                }else {
                    competition.setLocation(line);
                    competitions.add(competition);
                    i = -1;
                }
                i++;
            }
            return competitions;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    @Override
    protected void onPostExecute(ArrayList<Competition> competitions) {
        dialog.cancel();
    }
}
