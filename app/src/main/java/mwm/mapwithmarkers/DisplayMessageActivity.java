package mwm.mapwithmarkers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class DisplayMessageActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra("whatsapppp");

        // Capture the layout's TextView and set the string as its text
        this.textView = (TextView) findViewById(R.id.textView);
        this.textView.setText("alooooojaaaaaaaa");
    }

    /**
     * Called when the user taps the Send button
     */
    public void showTheMap(View view) {
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(intent);
    }

    /**
     * Makes a HTTP request.
     *
     * @param view
     */
    public void makeRequest(View view) {
        String url = "http://bb1c129b.ngrok.io/cars";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("Response => " + response.toString());
                //findViewById(R.id.progressBar1).setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("Error occurred");

            }
        });

        queue.add(jsObjRequest);
    }
}
