package de.nplusc.cc86.newtest;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;


public class MainActivity extends Activity implements View.OnClickListener{
TextView hw;
MapView mv;
GoogleMap  mmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupmap();
        //hw=(TextView) findViewById(R.id.textView);
       // mv=MapView
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupmap();
    }

    private void setupmap()
    {

        if(mmap==null)
        {
            FragmentManager f = getFragmentManager();
            MapFragment frg = (MapFragment) f.findFragmentById(R.id.mmap);
            Log.d("ALZR",frg + "");
            mmap = frg .getMap();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button)
        {
            mmap.addMarker(new MarkerOptions().position(new LatLng(0.0,0.0)).title("Test"));
        }
        else
        {
            InputStream raw = getResources().openRawResource(R.raw.kastanien);

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(raw, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                try {
                    JSONObject baeume = new JSONObject(result);
                    //{"type":"Feature","properties":{"BAUM_ID":100060470,"BOTANISCHE":"Aesculus carnea 'Briotii'","BAUMART":"Rotblühende Rosskastanie","PFLANZJAHR":2003,
                    // "KRONE_DM":"2 m","STAMMUMFAN":"25 cm","STANDORT":"Paulinenplatz 9"},"geometry":{"type":"MultiPoint","coordinates":[[9.963283171551076,53.55416115124533]]}}
                    JSONArray baumliste = baeume.getJSONArray("features");
                    for (int i = baumliste.length(); i <baumliste.length() ; i++) {
                        JSONObject baum = baumliste.getJSONObject(i);
                        JSONObject positionwrapped = baum.getJSONObject("geometry");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
