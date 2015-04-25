package fi.maplepoint.gpsorienteering;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Jukkis on 21.4.2015.
 */
public class controlTimingActivity extends AsyncTask<String, Void, String> {
    TextView statusfield;
    String[] array;
    public  controlTimingActivity(TextView text){
        statusfield = text;
    }
    protected void onPreExecute() {
    }
    @Override
    protected String doInBackground(String... arg0) {
        try {
            String cid = arg0[0];
            String uid = arg0[1];
            String cnum = arg0[2];
            String legtime = arg0[3];
            String controltime = arg0[4];
            String uuid = arg0[5];

            String link = "http://outdoorathletics.fi/gps-timing/libs/add_ctime.php";
            String data = URLEncoder.encode("cid", "UTF-8")
                    + "=" + URLEncoder.encode(cid, "UTF-8");
            data += "&" + URLEncoder.encode("uid", "UTF-8")
                    + "=" + URLEncoder.encode(uid, "UTF-8");
            data += "&" + URLEncoder.encode("cnum", "UTF-8")
                    + "=" + URLEncoder.encode(cnum, "UTF-8");
            data += "&" + URLEncoder.encode("legtime", "UTF-8")
                    + "=" + URLEncoder.encode(legtime, "UTF-8");
            data += "&" + URLEncoder.encode("controltime", "UTF-8")
                    + "=" + URLEncoder.encode(controltime, "UTF-8");
            data += "&" + URLEncoder.encode("uuid", "UTF-8")
                    + "=" + URLEncoder.encode(uuid, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(conn.getInputStream()));
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
    }
}
