package fi.maplepoint.gpsorienteering;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jukkis on 21.4.2015.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class SigninActivity extends AsyncTask<String, Void, String[]> {
    private ProgressDialog dialog;

    private TextView statusField;
    private Context context;


    public SigninActivity(Context context, TextView statusField) {
        this.statusField = statusField;
        this.context = context;
    }

    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(true);
        dialog.setMessage("Loading data...");
        dialog.show();
    }

    @Override
    protected String[] doInBackground(String... arg0) {
        try {
            String username = arg0[0];
            String password = arg0[1];
            String link = "http://outdoorathletics.fi/gps-timing/libs/login.php";
            String data = URLEncoder.encode("username", "UTF-8")
                    + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(password, "UTF-8");
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
            String[] array = new String[4];
            Integer i = 0;
            while ((line = reader.readLine()) != null) {
                array[i] = line;
                i++;
            }
            return array;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        dialog.cancel();
        if (result[0] != null) {
            Intent i = new Intent(context, selectCourseActivity.class);
            i.putExtra("Runner", result);
            context.startActivity(i);
        } else {
            statusField.setText("Invalid username or password!");
            statusField.setVisibility(View.VISIBLE);
        }
    }
}
