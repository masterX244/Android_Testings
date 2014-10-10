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

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;

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
            addTrees();
        }
        else
        {
            //mmap.
        }
    }

    public void addTrees() {
        FeatureCollection fc = CastaneaReader.with(this).read();

        int maxCounter = 0;
        for (Feature feature : fc) {
            MultiPoint point = (MultiPoint)feature.getGeometry();
            LngLatAlt position = point.getCoordinates().get(0);

            mmap.addMarker(new MarkerOptions().position(new LatLng(position.getLatitude(), position.getLongitude())).title("Marker"));

            if (maxCounter++ > 20) {
                break;
            }

        }

    }
}
