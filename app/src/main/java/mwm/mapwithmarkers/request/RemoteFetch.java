package mwm.mapwithmarkers.request;

/**
 * Created by Pablo on 7/26/2017.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteFetch {

    private static final String OPEN_WEATHER_MAP_API = "http://f61e84f9.ngrok.io/markers";

    public static JSONObject getJSON(String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.app_id));

            if (connection.getResponseCode() != 200) {
                return null;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
//            // successful
//            if (data.getInt("cod") != 200) {
//                return null;
//            }



            return data;
        } catch (Exception e) {
            Log.d("error", e.getMessage());
            //return e;

            return null;
        }
    }
}